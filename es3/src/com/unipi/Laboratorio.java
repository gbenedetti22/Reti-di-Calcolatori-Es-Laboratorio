package com.unipi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Laboratorio {
    private ArrayList<Boolean> computers;   //computers[i] = true -> computer in uso
                                            // computers[i] = false -> computer non in uso
    private Lock lock;
    Condition freeComputer;

    public Laboratorio() {
        computers = new ArrayList<>(Arrays.asList(new Boolean[20]));
        Collections.fill(computers, Boolean.FALSE);
        lock = new ReentrantLock();
        freeComputer = lock.newCondition();
    }

    //Il professore attende solo che tutta l aula sia vuota,
    // in quanto necessita di tutto il laboratorio come da specifica
    public void occupy(String msg) throws InterruptedException {
        try{
            lock.lock();
            while (computers.contains(Boolean.TRUE)){   //controllo che non ci sia nessuno dentro al laboratorio
                freeComputer.await();
            }

            Collections.fill(computers, Boolean.TRUE);  //"uso" tutti i computer
            System.out.println(msg);
            Thread.sleep(3000);

            Collections.fill(computers, Boolean.FALSE);
            freeComputer.signal();  //segnalo agli altri utenti che il laboratorio è libero
        }finally {
            lock.unlock();
        }
    }

    /*
     * La tecnica usata per garantire parallelismo tra studenti e tesisti,
     * si basa sul concetto di "lockare" la regione in cui viene effettuata la scelta di un computer.
     * Di fatto, una volta settato computers[i] = true, un eventuale altro utente
     * non potrà scegliere quell i-esimo computer.
     *
     * Lo studente attende solo se non ci sono computers liberi
     * */
    public void useComputer(String msg) throws InterruptedException {
        try {
            lock.lock();
            while(!computers.contains(Boolean.FALSE)){  // controllo che ci siano computers liberi
                freeComputer.await();
            }

            int index = computers.indexOf(Boolean.FALSE);   //occupo il primo computer libero
            computers.set(index, Boolean.TRUE);
            lock.unlock();

            System.out.println("[Computer "+index+"] "+msg);
            Thread.sleep(3000);

            lock.lock();
            computers.set(index, Boolean.FALSE);
            freeComputer.signal();
        }finally {
            lock.unlock();
        }

    }

    //Il tesista attende solo se il suo i-esimo computer è già in uso
    public void useComputer(String msg, int i) throws InterruptedException {
        try {
            lock.lock();

            while (computers.get(i)) {
                freeComputer.await();
            }

            computers.set(i, Boolean.TRUE);
            lock.unlock();

            System.out.println(msg);
            Thread.sleep(3000);

            lock.lock();
            computers.set(i, Boolean.FALSE);
            freeComputer.signal();
        }finally {
            lock.unlock();
        }
    }
}
