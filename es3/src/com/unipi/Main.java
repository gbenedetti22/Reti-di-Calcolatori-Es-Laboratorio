package com.unipi;

import java.util.ArrayList;
import java.util.Collections;

public class Main {
    private static final int N = 12; // numero totale di studenti + professori + tesisti

    public static void main(String[] args) throws InterruptedException {
        ArrayList<Utente> utenti = newUsersArray(new Laboratorio());
        printUtenti(utenti);

        for (Utente utente : utenti) {
            utente.start();
        }

        for (Utente utente : utenti) {
            utente.join();
        }
    }

    //La creazione dell array degli studenti è pesata. Dato un valore N:
    //  - il 25% di N sono professori
    //  - il 33% di N sono tesisti
    //  - il 40% di N sono studenti
    //  Questo per simulare maggiormente l esercizio
    private static ArrayList<Utente> newUsersArray(Laboratorio lab) {
        ArrayList<Utente> utenti = new ArrayList<>(N);

        int numProfessori = (25 * N) / 100;
        int numTesisti = ((33 * N) / 100) + 1;
        int numStudenti = ((40 * N) / 100) + 1;

        System.out.println("Professori: " + numProfessori);
        System.out.println("Tesisti: " + numTesisti);
        System.out.println("Studenti: " + numStudenti);

        for (int i = 0; i < numProfessori; i++) {
            Utente prof = new Utente(USERS.PROFESSORE, lab);
            prof.setPriority(Thread.MAX_PRIORITY);

            utenti.add(prof);
        }

        for (int i = 0; i < numTesisti; i++) {
            Utente tesista = new Utente(USERS.TESISTA, lab);
            tesista.setPriority(Thread.NORM_PRIORITY);

            utenti.add(tesista);
        }

        for (int i = 0; i < numStudenti; i++) {
            Utente studente = new Utente(USERS.STUDENTE, lab);
            studente.setPriority(Thread.MIN_PRIORITY);

            utenti.add(studente);
        }

        Collections.shuffle(utenti);
        return utenti;
    }

    private static void printUtenti(ArrayList<Utente> utenti) {
        for (Utente utente : utenti) {
            System.out.print(utente.getType());
            System.out.print(", ");
        }
        System.out.println();
        System.out.println();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
