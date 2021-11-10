package com.unipi;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final Socket client;
    private final String BASE_FILE_PATH = "src/com/unipi/";

    public RequestHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             OutputStream out = client.getOutputStream()) {
            int flength = 0;

            //leggo la richiesta e ottengo il nome del file
            String request = in.readLine();
            String filename = request.split(" ")[1].trim().replace("/", "");
            FileInputStream index;

            //se la richiesta è vuota, restituisco index.html
            if (filename.isEmpty()) {
                System.out.printf("%s -> Ricevuta richiesta: index.html\n", Thread.currentThread().getName());

                File requestedFile = new File(BASE_FILE_PATH + "index.html");
                flength = (int) requestedFile.length();

                index = new FileInputStream(requestedFile);
                filename = BASE_FILE_PATH + "index.html";

            } else {  //se la richiesta non è vuota
                filename = BASE_FILE_PATH.concat(filename);

                //controllo che il file esista. Se esiste lo prendo
                File requestedFile = getFile(out, filename);
                if (requestedFile == null) return;

                System.out.printf("%s -> Ricevuta richiesta file: %s\n", Thread.currentThread().getName(), filename);

                index = new FileInputStream(requestedFile);
                flength = (int) requestedFile.length();
            }

            //costruisco la risposta (l header)
            StringBuilder header = getHTMLHeader(filename);

            byte[] responseContent = new byte[flength];

            if (index.read(responseContent) == -1) {
                System.out.printf("%s -> Impossibile leggere il file\n", Thread.currentThread().getName());
                out.close();
                index.close();
                return;
            }

            System.out.printf("%s -> Invio il file %s\n", Thread.currentThread().getName(), filename);
            out.write(header.toString().getBytes());    //invio prima l header
            out.write(responseContent); //e poi il file

            index.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private StringBuilder getHTMLHeader(String filename) {
        String fileExtension = filename.split("\\.")[1];
        StringBuilder header = new StringBuilder(HTML_HEADERS.OK.message());

        switch (fileExtension) {
            case "txt" -> header.append("Content-Type: text/plain\r\n");
            case "jpg" -> header.append("Content-Type: image/jpeg\r\n");
            case "html" -> header.append("Content-Type: text/html\r\n");
            case "gif" -> header.append("Content-Type: image/gif\r\n");
            default -> {
            }
        }
        header.append("\r\n");
        return header;
    }

    private File getFile(OutputStream out, String filename) throws IOException {
        File requestedFile = new File(filename);
        // se il file non esiste, mando un error code 404 e ritorno
        if (!requestedFile.exists()) {
            String notFoundHeader = HTML_HEADERS.NOT_FOUND.message();
            System.out.printf("%s -> File: %s non esiste\n", Thread.currentThread().getName(), filename);

            out.write(notFoundHeader.getBytes());
            return null;
        }
        return requestedFile;
    }
}
