package com.unipi;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        Random random = new Random();
        int n_iterations = random.nextInt(10000);

        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 23456);
            CongressInterface congress = (CongressInterface) registry.lookup("congress-app");
            for (int i = 0; i < n_iterations; i++) {
                String randomName = getRandomName();
                int day = random.nextInt(3);
                int slot = random.nextInt(12);

                boolean b = congress.registerSpeaker(randomName, day, slot);
                if(!b)
                    System.out.println("Giorno "+ day+ ", slot: "+ slot+" invalido");
            }

            System.out.println("\n\n");
            ArrayList<LinkedList<LinkedList<String>>> days = congress.getProgram();
            int n_day=0;
            int n_slot=0;
            for (LinkedList<LinkedList<String>> slots : days){
                System.out.println("Giorno: "+n_day);
                for(LinkedList<String> speakers : slots){
                    System.out.print("Slot: "+n_slot+" -> ");
                    for (String speaker : speakers){
                        System.out.print(speaker+" ");
                    }
                    System.out.println();
                    n_slot++;
                }
                n_day++;
                System.out.println("\n");
            }

        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }

    }

    private static String getRandomName(){
        String[] names = new String[]{"pippo", "pluto", "paperino", "qui", "quo", "qua", "minnie"};
        return names[new Random().nextInt(names.length)];
    }
}
