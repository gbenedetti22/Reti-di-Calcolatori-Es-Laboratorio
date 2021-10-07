package com.unipi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

public class Laboratorio {
    private ArrayList<Boolean> computers;   //computers[i] = true -> computer in uso
    // computers[i] = false -> computer non in uso
    private HashSet<Utente> profList;

    public Laboratorio(int numProfessori) {
        computers = new ArrayList<>(Arrays.asList(new Boolean[20]));
        Collections.fill(computers, Boolean.FALSE);

        profList = new HashSet<>(numProfessori);
    }

    public synchronized void occupy(String msg) throws InterruptedException {
        while (computers.contains(Boolean.TRUE)) {   //controllo che non ci sia nessuno dentro al laboratorio
            profList.add((Utente) Thread.currentThread());
            wait();
        }

        Collections.fill(computers, Boolean.TRUE);  //"uso" tutti i computer
        profList.remove((Utente) Thread.currentThread());

        System.out.println(msg);
        Thread.sleep(3000);

        Collections.fill(computers, Boolean.FALSE);
        notify();  //segnalo agli altri utenti che il laboratorio è libero
    }

    public void useComputer(String msg) throws InterruptedException {
        int index;
        synchronized (this) {
            while (!computers.contains(Boolean.FALSE)) {  // controllo che ci siano computers liberi
                wait();
            }

            //se ci sono professori in attesa, dò la precedenza a loro
            while (!profList.isEmpty()) {

                /*  Può capitare che un professore non riesca ad acquisire la lock, andando in stato di BLOCKED.
                    In tal caso la notify sveglierebbe SOLO thread non professori, causando risvegli alternati tra gli
                    altri thread (la notify funziona solo su thread in stato di WAITING).

                    Per evitare ciò, uso la notify solo se non ci sono thread professori in stato di BLOCKED.
                    Se ci sono professori bloccati, allora gli altri utenti si mettono semplicemente in wait, lasciando
                    che i professori acquisiscano la lock. Sarò compito dei professori togliersi dalla lista profList()
                    e notificare gli altri utenti.
                */
                if (profList.stream().noneMatch(p -> p.getState() == Thread.State.BLOCKED)) {
                    notify();
                }
                wait();
            }

            index = computers.indexOf(Boolean.FALSE);   //occupo il primo computer libero
            computers.set(index, Boolean.TRUE);
        }

        System.out.println("[Computer " + index + "] " + msg);
        Thread.sleep(3000);

        computers.set(index, Boolean.FALSE);

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

            while (!profList.isEmpty()) {
                if (profList.stream().noneMatch(p -> p.getState() == Thread.State.BLOCKED)) {
                    notify();
                }
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
