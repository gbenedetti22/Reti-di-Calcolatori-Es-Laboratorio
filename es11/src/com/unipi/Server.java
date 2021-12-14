package com.unipi;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;

public class Server extends UnicastRemoteObject implements CongressInterface {
    private final int MAX_DAYS = 3;
    private final int MAX_SESSIONS = 12;
    private final int MAX_SPEAKERS = 5;

    private final ArrayList<LinkedList<LinkedList<String>>> days = new ArrayList<>(MAX_DAYS);

    protected Server() throws RemoteException {
        super();
        for (int i = 0; i < MAX_DAYS; i++) {
            LinkedList<LinkedList<String>> slots = new LinkedList<>();
            days.add(slots);
            for (int j = 0; j < MAX_SESSIONS; j++) {
                slots.add(new LinkedList<>());
            }
        }
    }

    @Override
    public synchronized boolean registerSpeaker(String name, int day, int slot) throws RemoteException {
        if(day >= MAX_DAYS || slot >= MAX_SESSIONS)
            return false;


        LinkedList<LinkedList<String>> slots = days.get(day);   //prendo gli slots di quel giorno
        LinkedList<String> speakers = slots.get(slot);          //prendo gli speaker registrati per quel giorno

        if (speakers.size() >= MAX_SPEAKERS) {    //se il numero di speakers per quello slot è maggiore di MAX_SPEAKERS..
            return false;
        }

        speakers.add(name);
        return true;
    }

    @Override
    public synchronized ArrayList<LinkedList<LinkedList<String>>> getProgram() throws RemoteException {
        return days;
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            String name = "congress-app";

            Registry registry = LocateRegistry.createRegistry(23456);
            registry.bind(name, server);

            System.out.println("Server Online");
            System.in.read();

            UnicastRemoteObject.unexportObject(server, false);
            registry.unbind(name);
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
