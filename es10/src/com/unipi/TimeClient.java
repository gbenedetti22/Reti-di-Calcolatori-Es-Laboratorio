package com.unipi;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class TimeClient extends Thread{
    InetAddress address;
    int port;

    public TimeClient(String address, int port) {
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
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(port);
            socket.joinGroup(address);
            DatagramPacket p = new DatagramPacket(new byte[1024], 1024);
            while (!isInterrupted()) {
                socket.receive(p);
                System.out.println(new String(p.getData(), 0, p.getLength()));
            }
            socket.close();
        } catch (IOException e){
            System.err.println("Errore nell invio dei pacchetti, provare ad utilizzare un indirizzo diverso");
            System.err.println(e.getMessage());
            if(socket != null)
                socket.close();
        }

    }
}
