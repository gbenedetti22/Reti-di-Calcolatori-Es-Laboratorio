package com.unipi;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String[] persons = {"John", "Mike", "Gabriele", "Marco", "Matteo", "Topolino"};
    private static final int N_THREAD_WORKERS = 2;
    private static final int N_PERSONS = 10;
    private static final int K = 2;

    public static void main(String[] args) {
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(K); //coda sala 2
        LinkedList<Person> sala1 = new LinkedList<>();  //sala d'attesa

        ExecutorService pool = new ThreadPoolExecutor(N_THREAD_WORKERS, N_THREAD_WORKERS,
                0L, TimeUnit.MILLISECONDS,
                queue);

        int servedPeople = 0;

        while (servedPeople < N_PERSONS) {
            Person person = new Person(persons[new Random().nextInt(persons.length)]); //persona con nome random
            sala1.add(person);  // simulo l entrata della persona x all interno delle Poste

            if(queue.size() != K){  //se la coda non è piena
                pool.submit(sala1.poll());  //la persona x si mette in fila nella sala 2
                servedPeople++;
            }
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
