package com.unipi;

import java.io.IOException;

public class MainClient {
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

        TimeClient timeClient = new TimeClient(address, port);
        timeClient.start();

        System.out.println("Premi un pulsante per uscire...");
        System.out.println();
        try {
            System.in.read();
            timeClient.interrupt();
            timeClient.join();
        } catch (InterruptedException | IOException e) {

        }
    }
}
