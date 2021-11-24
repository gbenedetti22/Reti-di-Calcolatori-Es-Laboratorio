package com.unipi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class EchoClient {
    public static void main(String[] args) {
        try {
            SocketChannel client = SocketChannel.open(new InetSocketAddress("localhost", 8080));
            client.configureBlocking(true);

            ByteBuffer buffer = ByteBuffer.allocate(1024);

            Scanner scanner = new Scanner(System.in);
            String input;

            System.out.println("Inserire parole o frasi...");
            System.out.println();
            while (!(input = scanner.nextLine()).trim().equalsIgnoreCase("quit") && !input.isEmpty()) {
                if(input.isBlank())
                    continue;

                System.out.println("Invio: " + input);
                sendTo(client, input, buffer);

                String output = receiveFrom(client, buffer);
                buffer.clear();

                System.out.println("Ricevuto: " + output);
                System.out.println();
            }

            sendTo(client, "quit", buffer);
            client.close();
        } catch (IOException e) {
            System.out.println("Server disconnesso");
        }
    }

    private static String receiveFrom(SocketChannel client, ByteBuffer buffer) throws IOException {
        int nBytes = client.read(buffer);
        buffer.flip();
        return new String(buffer.array(), 0, nBytes);
    }

    private static void sendTo(SocketChannel client, String s, ByteBuffer buffer) throws IOException {
        buffer.put(s.getBytes());
        buffer.flip();

        while(buffer.hasRemaining()) client.write(buffer);
        buffer.clear();
    }
}
