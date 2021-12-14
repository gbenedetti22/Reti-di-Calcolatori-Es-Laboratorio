package com.unipi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;

public interface CongressInterface extends Remote {
    boolean registerSpeaker(String name, int day, int slot) throws RemoteException;
    ArrayList<LinkedList<LinkedList<String>>> getProgram()  throws RemoteException;
}
