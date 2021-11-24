package com.unipi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeServer extends Thread {
    private InetAddress address;
    private final int port;

    public TimeServer(String address, int port) {
        this.port = port;

        try {
            this.address = InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            System.err.println("Indirizzo ip errato");
            return;
        }

        if(!this.address.isMulticastAddress()){
            System.err.println("L indizzo inserito non è un indirizzo di Multicast");
            this.address = null;
        }
    }

    @Override
    public void run() {
        if(address == null)
            return;

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            while (!isInterrupted()) {
                String s = getDateAndHour();
                DatagramPacket packet = new DatagramPacket(s.getBytes(), s.length(), address, port);
                socket.send(packet);
                sleep(1000);
            }
            socket.close();

        }catch (InterruptedException interrupted){
            socket.close();
        }catch (IOException e){
            System.err.println("Errore nell invio dei pacchetti, provare ad utilizzare un indirizzo diverso");
            System.err.println(e.getMessage());
            if(socket != null)
                socket.close();
        }

    }

    private String getDateAndHour() {
        return LocalDate.now().toString().replaceAll("-", "/") + " " + LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss"));
    }

}
