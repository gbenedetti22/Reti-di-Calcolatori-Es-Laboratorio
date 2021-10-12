package com.unipi;

import java.util.Random;

public class Person implements Runnable {
    private int workTime;
    private String name;
    private String[] todos = {"fare la Postepay",
            "pagare un bollettino",
            "inviare una raccomandata",
            "inviare una lettera",
            "spedire un pacco",
            "ricaricare la Postepay"};

    public Person(String name) {
        this.name = name;
        workTime = new Random().nextInt() % 10000;
        workTime = Math.abs(workTime);
    }

    @Override
    public void run() {
        int todo = new Random().nextInt(todos.length);

        System.out.println(name + " deve: " + todos[todo] +" -> "+ Thread.currentThread().getName());
        try {
            Thread.sleep(workTime);
        } catch (InterruptedException e) {
            System.out.println(name + " è molto adirato per essere stato interrotto. Farà causa alla compagnia");
            return;
        }

        System.out.println(name + " ha finito di "+ todos[todo] + "\n");
    }
}
