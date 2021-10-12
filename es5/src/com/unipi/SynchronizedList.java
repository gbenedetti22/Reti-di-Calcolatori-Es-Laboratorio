package com.unipi;


import java.util.LinkedList;

public class SynchronizedList {
    private LinkedList<String> list;

    public SynchronizedList() {
        list = new LinkedList<>();
    }

    public void add(String value){
        synchronized (this){
            list.add(value);
            notify();
        }
    }

    public synchronized String getOrWait() {
        while (list.isEmpty()){
            try {
                wait();
            } catch (InterruptedException e) {
                return null;
            }
        }

        String dir = list.poll();
        if(dir == null)
            add(null);

        return dir;
    }
}
