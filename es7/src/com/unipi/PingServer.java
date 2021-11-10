package com.unipi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Random;

public class PingServer implements Runnable {
    private final int PORT;
    private final Random random;
    private final long DELAY;
    DatagramSocket socket;

    public PingServer(int PORT, long delay) {
        this.PORT = PORT;
        this.DELAY = delay;
        random = new Random();
    }


    @Override
    public void run() {
        try {
            byte[] buffer = new byte[1024];

            socket = new DatagramSocket(PORT);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            while (true) {
                socket.receive(packet);

                // simulo la probabilitò di perdita dei pacchetti del 25% (1/4 = 0.25)
                String msg = new String(packet.getData(), 0, packet.getLength());

                if (random.nextInt(4) != 1) {
                    Thread.sleep(DELAY);

                    System.out.println(packet.getAddress().toString() +
                            ":" + packet.getPort() +
                            " -> " + msg + ": ECHO");
                    socket.send(packet);
                } else {
                    System.out.println(packet.getAddress().toString() +
                            ":" + packet.getPort() +
                            " -> " + msg + ": SCARTO");
                }

                Arrays.fill(buffer, (byte) 0);
            }
        } catch (IOException | InterruptedException ignored) {
        }

    }

    public void close() {
        socket.close();
    }
}
