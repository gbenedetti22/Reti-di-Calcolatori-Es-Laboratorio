package com.unipi;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

public class PingClient implements Runnable {
    private final int TIMEOUT;
    private InetAddress address;
    private final int PORT;
    private ArrayList<Long> rtts;

    public PingClient(String address, int PORT) {
        this.TIMEOUT = 2000;
        this.PORT = PORT;
        this.rtts = new ArrayList<>(10);
        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT);

            byte[] buf = new byte[1024];
            DatagramPacket receive = new DatagramPacket(buf, buf.length, address, PORT);

            for (int i = 0; i < 10; i++) {
                StringBuilder message = new StringBuilder();

                message.append("PING ").append(i).append(" ").append(System.currentTimeMillis());

                DatagramPacket packet = new DatagramPacket(message.toString().getBytes(), message.length(), address, PORT);

                socket.send(packet);
                int seq = -1;

                String response="";
                try {
                    // ciclo finchè non ricevo il pacchetto inviato.
                    // Se arriva un pacchetto con un seq diverso da quello inviato,
                    // mi rimetto in ascolto fino allo scadere del timeout
                    while (seq != i) {
                        socket.receive(receive);
                        response = new String(receive.getData(), 0, receive.getLength());

                        seq = Integer.parseInt(response.split(" ")[1]);
                        Arrays.fill(buf, (byte) 0);

//                        if(seq != i)
//                            System.out.println("Ricevuto pacchetto ritardatario: " + seq);
                    }
                } catch (SocketTimeoutException e) {
                    System.out.println(message + "-> RTT: *");
                    continue;
                }

                long currentTime = System.currentTimeMillis();

                //dal messaggio: "PING seq timestamp" ottengo il timestamp
                long packetTime = Long.parseLong(response.split(" ")[2]);

                long rtt = currentTime - packetTime;
                System.out.println(message + "-> RTT: " + rtt + "ms");
                rtts.add(rtt);
            }

            printStatistics();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printStatistics() {
        System.out.println();
        System.out.println("---- PING Statistics ----");
        int packetReceived = rtts.size();
        int packetLost = (10 - rtts.size()) * 10;

        try {
            long maxRtt = Collections.max(rtts);
            long minRtt = Collections.min(rtts);
            double avgRtt = rtts.stream().mapToDouble(e -> e).average().orElse(0.0);
            String avgStr = String.format("%.2f", avgRtt);

            System.out.println("10 packets transmitted, " + packetReceived + " packets received, " + packetLost + "% packet lost");
            System.out.println("round-trip (ms) min/avg/max = " + minRtt + "/" + avgStr + "/" + maxRtt);
            System.out.println();
        } catch (NoSuchElementException e) {
            System.out.println("Nessun pacchetto ricevuto");
        }
    }
}
