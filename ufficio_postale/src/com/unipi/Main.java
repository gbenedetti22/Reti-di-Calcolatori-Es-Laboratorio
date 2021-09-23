package com.unipi;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final String[] persons = {"John", "Mike", "Gabriele", "Marco", "Matteo", "Topolino"};
    private static final int N_PERSONS = 10;

    public static void main(String[] args) {
        /* Per la richiesta facoltativa, basta usare un cachedThreadPool
        *  oppure farsi un nuovo ThreadPoolExecutor specificando come queue la SynchronousQueue
        *  e come tempo massimo, un valore arbitrario a scelta
        * */
        ExecutorService pool = Executors.newFixedThreadPool(4);

        for (int i = 0; i < N_PERSONS; i++) {
            int person = new Random().nextInt(persons.length);

            pool.submit(new Person(persons[person]));
        }

        pool.shutdown();
        try {
            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
