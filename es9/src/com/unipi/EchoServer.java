package com.unipi;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;

public class EchoServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(PORT));
        serverSocket.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("Server Online");
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (selector.select() != 0) {
            Set<SelectionKey> keys = selector.selectedKeys();

            for (SelectionKey key : keys) {
                if (key.isAcceptable()) {
                    SocketChannel newClient = serverSocket.accept();
                    newClient.configureBlocking(false);

                    newClient.register(selector, SelectionKey.OP_READ);
                    System.out.println("Nuovo client " + newClient.getRemoteAddress().toString());
                } else if (key.isReadable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    serve(client, buffer);
                } else if (key.isWritable()) {
                    System.out.println("Pippo");
                }

                keys.remove(key);
            }
        }

        selector.close();
        serverSocket.close();
    }

    private static void serve(SocketChannel client, ByteBuffer buffer) throws IOException {
        try {
            String request = receiveFromClient(client, buffer);
            if (request == null) return;
            if (request.equalsIgnoreCase("quit")) {
                System.out.println(client.getRemoteAddress().toString() + ": disconnesso");
                client.close();
                return;
            }
            System.out.println(request);

            request = request.concat(" echoed by Server");
            sendToClient(client, request, buffer);
        } catch (IOException e) {
            System.out.println(client.getRemoteAddress().toString() + ": disconnesso");
            client.close();
        }

    }

    private static void sendToClient(SocketChannel client, String request, ByteBuffer buffer) throws IOException {
        buffer.clear();
        buffer.put(request.getBytes());
        buffer.flip();
        client.write(buffer);
    }

    private static String receiveFromClient(SocketChannel client, ByteBuffer buffer) throws IOException {
        buffer.clear();
        int nBytes = client.read(buffer);

        if (nBytes == -1) {
            System.out.println("Errore nella lettura del client " + client.getRemoteAddress().toString());
            client.close();
            return null;
        }
        return new String(buffer.array(), 0, nBytes);
    }
}
