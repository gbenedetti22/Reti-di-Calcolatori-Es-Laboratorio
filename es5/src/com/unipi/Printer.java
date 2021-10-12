package com.unipi;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Printer {
    private PrintWriter writer;

    public Printer(String pathname) {
        try {
            writer = new PrintWriter(pathname);
        } catch (FileNotFoundException e) {
            writer = null;
            System.err.println("Impossibile creare il file di output");
        }
    }

    public synchronized void println(String s){
        if(writer == null)
            return;

        writer.append(s).append("\n");
    }

    public void close(){
        writer.flush();
        writer.close();
    }
}
