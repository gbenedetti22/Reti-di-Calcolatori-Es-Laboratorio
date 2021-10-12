package com.unipi;

import java.io.File;

public class Producer extends Thread{
    private String entry;
    private SynchronizedList list;

    public Producer(String startDir, SynchronizedList list) {
        this.entry = startDir;
        this.list = list;
    }

    @Override
    public void run() {
        if(entry == null){
            list.add(null); //per far terminare i Thread Consumer
            return;
        }

        File entryPoint = new File(entry);

        if(!entryPoint.exists() || !entryPoint.isDirectory()){
            System.err.println("La directory non esiste o non è una directory");
            return;
        }

        scan(entryPoint);
        list.add(null);
    }

    private void scan(File path) {
        File[] fileList = path.listFiles();
        if (fileList == null){
            return;
        }

        for(File cf : fileList){
            if(cf.isDirectory()){
                list.add(cf.getAbsolutePath());
                scan(cf);
            }
        }
    }
}
