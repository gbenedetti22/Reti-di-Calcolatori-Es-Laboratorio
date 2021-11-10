package com.unipi;

import java.util.Scanner;

public class MainServer {

    public static void main(String[] args) throws InterruptedException {
        int[] argsParsed = parseArgs(args);
        if(argsParsed == null){
            System.out.println("Errore nell inserimento dei comandi");
            return;
        }

        PingServer server = new PingServer(argsParsed[0], argsParsed[1]);
        Thread thread = new Thread(server);

        thread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Server Online");
        System.out.println("Scrivere ESC per uscire");
        String input = "";

        while (!input.equalsIgnoreCase("esc")){
            input = scanner.next();
        }

        server.close();
        thread.join();
    }

    private static int[] parseArgs(String[] args){
        int[] command = new int[2];

        switch (args.length){
            case 1 -> {
                try {
                    command[0] = Integer.parseInt(args[0]);
                    command[1] = 1000;

                }catch (NumberFormatException e){
                    return null;
                }
            }

            case 2 -> {
                try {
                    command[0] = Integer.parseInt(args[0]);
                    command[1] = Integer.parseInt(args[1]);

                }catch (NumberFormatException e){
                    return null;
                }
            }
        }

        return command;
    }
}
