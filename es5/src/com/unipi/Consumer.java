package com.unipi;

import java.io.File;

public class Consumer extends Thread {
    private SynchronizedList list;
    private Printer printer;

    public Consumer(SynchronizedList list, Printer printer) {
        this.list = list;
        this.printer = printer;
    }

    @Override
    public void run() {
        String dir;
        do {
            dir = list.getOrWait();
            if (dir != null) {
                printDir(dir);
            }

        } while (dir != null);
    }

    private void printDir(String dirPath) {
        File dir = new File(dirPath);

        File[] files = dir.listFiles();
        if (files == null) {
            System.out.println(dir.getName() + ": []");
            return;
        }

        StringBuilder builder = new StringBuilder(files.length);
        builder.append(dir.getName()).append(": ");

        String prefix = "";
        for (File file : files) {
            if(file.isFile()){
                builder.append(prefix);
                prefix = ", ";
                builder.append(file.getName());
            }
        }

        System.out.println(builder);
        printer.println(builder.toString());
    }
}
