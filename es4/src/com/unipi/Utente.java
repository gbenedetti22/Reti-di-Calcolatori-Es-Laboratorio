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
        K = ThreadLocalRandom.current().nextInt(4) + 1;

        if (type == USERS.TESISTA)
            i = ThreadLocalRandom.current().nextInt(20);
    }

    @Override
    public void run() { // -> tutor
        int temp = K;
        while (K > 0) {
            try {
                switch (type) {
                    case PROFESSORE -> {
                        lab.occupy("PROFESSORE occupa il Laboratorio -> " + getName() + " | K = "+ temp);
                    }

                    case TESISTA -> {
                        String msg = "TESISTA sta usando il computer " + i + " -> " + getName() + " | K = "+ temp;
                        lab.useComputer(msg, i);
                    }

                    case STUDENTE -> {
                        String msg = "STUDENTE sta usando il Laboratorio -> " + getName() + " | K = "+ temp;
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
