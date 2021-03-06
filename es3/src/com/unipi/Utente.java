package com.unipi;

import java.util.concurrent.ThreadLocalRandom;

public class Utente extends Thread {
    private USERS type;
    private Laboratorio lab;
    private int i = -1;
    private int K;

    public Utente(USERS type, Laboratorio lab) {
        this.type = type;
        this.lab = lab;
        K = ThreadLocalRandom.current().nextInt(5);

        if (type == USERS.TESISTA)
            i = ThreadLocalRandom.current().nextInt(20);
    }

    @Override
    public void run() {
        while (K > 0) {
            try {
                switch (type) {
                    case PROFESSORE -> lab.occupy("PROFESSORE occupa il Laboratorio -> " + getName() + " | K = "+ K);

                    case TESISTA -> {
                        String msg = "TESISTA sta usando il computer " + i + " -> " + getName() + " | K = "+ K;
                        lab.useComputer(msg, i);
                    }

                    case STUDENTE -> {
                        String msg = "STUDENTE sta usando il Laboratorio -> " + getName() + " | K = "+ K;
                        lab.useComputer(msg);
                    }
                }

                K--;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public USERS getType() {
        return type;
    }
}
