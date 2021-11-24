package com.unipi;

import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {
        if(args.length != 2) {
            System.err.println("Inserire solo indirizzo ip e porta");
            return;
        }

        String address = args[0];
        int port = 8080;
        try {
            port = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            System.err.println("Porta inserita non corretta");
            System.err.println("Verrà usata la porta di default [8080]");
        }


        TimeServer timeServer = new TimeServer(address, port);
        timeServer.start();

        System.out.println("Premi un pulsante per uscire...");
        try {
            System.in.read();
            timeServer.interrupt();
            timeServer.join();
        } catch (InterruptedException | IOException e) {

        }
    }
}
