package com.unipi;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        if(args.length != 3){
            System.err.println("Inserire il numero di studenti, professori e tesisti correttamente");
            return;
        }

        try {
            int numProfessori = Integer.parseInt(args[0]);
            int numTesisti = Integer.parseInt(args[1]);
            int numStudenti = Integer.parseInt(args[2]);

            ArrayList<Utente> utenti = newUsersArray(new Laboratorio(), numProfessori, numTesisti, numStudenti);
            printUtenti(utenti);

            for (Utente utente : utenti) {
                utente.start();
            }

            for (Utente utente : utenti) {
                utente.join();
            }
        }catch (NumberFormatException e){
            System.err.println("Inserire il numero di studenti, professori e tesisti correttamente");
        }

    }


    private static ArrayList<Utente> newUsersArray(Laboratorio lab, int numProfessori, int numTesisti, int numStudenti) {
        ArrayList<Utente> utenti = new ArrayList<>(numProfessori + numStudenti + numTesisti);

        System.out.println("Professori: " + numProfessori);
        System.out.println("Tesisti: " + numTesisti);
        System.out.println("Studenti: " + numStudenti);

        for (int i = 0; i < numProfessori; i++) {
            Utente prof = new Utente(USERS.PROFESSORE, lab);

            utenti.add(prof);
        }

        for (int i = 0; i < numTesisti; i++) {
            Utente tesista = new Utente(USERS.TESISTA, lab);

            utenti.add(tesista);
        }

        for (int i = 0; i < numStudenti; i++) {
            Utente studente = new Utente(USERS.STUDENTE, lab);

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
