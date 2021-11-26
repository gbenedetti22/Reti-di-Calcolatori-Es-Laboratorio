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
    private static Selector selector;

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(PORT));
        serverSocket.configureBlocking(false);

        selector = Selector.open();
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
                    readRequest(client, buffer);

                } else if (key.isWritable()) {
                    SocketChannel client = (SocketChannel) key.channel();
                    String request = (String) key.attachment();

                    sendToClient(client, request, buffer);
                }

                keys.remove(key);
            }
        }

        selector.close();
        serverSocket.close();
    }

    private static void readRequest(SocketChannel client, ByteBuffer buffer) throws IOException {
        try {
            String request = receiveFromClient(client, buffer);
            if (request == null) return;
            if (request.equals("quit")) {
                System.out.println(client.getRemoteAddress().toString() + ": disconnesso");
                client.close();
                return;
            }
            System.out.println(request);

            request = request.concat(" echoed by Server");

            client.register(selector, SelectionKey.OP_WRITE, request);
        } catch (IOException e) {
            System.out.println(client.getRemoteAddress().toString() + ": disconnesso");
            client.close();
        }
    }

    private static void sendToClient(SocketChannel client, String request, ByteBuffer buffer) throws IOException {
        buffer.clear();
        buffer.put(request.getBytes());
        buffer.flip();
        while(buffer.hasRemaining())
            client.write(buffer);

        client.register(selector, SelectionKey.OP_READ);
    }

    private static String receiveFromClient(SocketChannel client, ByteBuffer buffer) throws IOException {
        buffer.clear();
        int nBytes;

        StringBuilder request = new StringBuilder();
        while((nBytes = client.read(buffer)) > 0){
            request.append(new String(buffer.array(), 0, nBytes));
        }

        if (nBytes == -1) {
            System.out.println(client.getRemoteAddress().toString() + ": disconnesso");
            client.close();
            return null;
        }

        return request.toString();
    }
}
