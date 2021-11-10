package com.unipi;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(6789);
        ExecutorService pool = Executors.newFixedThreadPool(10);

        System.out.println("Server Online");
        System.out.println();

        while (true){
            Socket client = serverSocket.accept();
            pool.submit(new RequestHandler(client));
        }

    }
}
