package com.unipi;

public class MainClient {

    public static void main(String[] args) throws InterruptedException {
        try {
            PingClient client = new PingClient(args[0], Integer.parseInt(args[1]));
            Thread thread = new Thread(client);

            thread.start();
            thread.join();

        }catch (NumberFormatException e){
            System.out.println("Porta inserita non corretta");
        }
    }
}
