package com.unipi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Laboratorio {
    private final ArrayList<Boolean> computers;   //computers[i] = true -> computer in uso
    // computers[i] = false -> computer non in uso

    public Laboratorio() {
        computers = new ArrayList<>(Arrays.asList(new Boolean[20]));
        Collections.fill(computers, Boolean.FALSE);
    }

    public synchronized void occupy(String msg) throws InterruptedException {
        while (computers.contains(Boolean.TRUE)) {   //controllo che non ci sia nessuno dentro al laboratorio
            wait();
        }

        Collections.fill(computers, Boolean.TRUE);  //"uso" tutti i computer
        System.out.println(msg);
        Thread.sleep(3000);

        Collections.fill(computers, Boolean.FALSE);
        notify();  //segnalo agli altri utenti che il laboratorio è libero
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
        int index;
        synchronized (this) {
            while (!computers.contains(Boolean.FALSE)) {  // controllo che ci siano computers liberi
                wait();
            }

            index = computers.indexOf(Boolean.FALSE);   //occupo il primo computer libero
            computers.set(index, Boolean.TRUE);
        }

        System.out.println("[Computer " + index + "] " + msg);
        Thread.sleep(3000);

        computers.set(index, Boolean.FALSE);    // è fuori dal sunchronized perchè sono sicuro che nessun altro utente
                                                // può avere quel computer

        synchronized (this) {
            notify();
        }

    }

    //Il tesista attende solo se il suo i-esimo computer è già in uso
    public void useComputer(String msg, int i) throws InterruptedException {
        synchronized (this) {
            while (computers.get(i)) {
                wait();
            }

            computers.set(i, Boolean.TRUE);
        }

        System.out.println(msg);
        Thread.sleep(3000);

        computers.set(i, Boolean.FALSE);
        synchronized (this) {
            notify();
        }
    }
}
