package com.unipi;

public class Main {

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Inserire accuracy e tempo di attesa!");
            return;
        }

        double accuracy;
        int time;

        try {
            accuracy = Double.parseDouble(args[0]);
            time = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.out.println("Numeri inseriti non validi");
            return;
        }

        PiGrecoCalculator piCalc = new PiGrecoCalculator(accuracy);
        Thread thread = new Thread(piCalc);
        thread.start();

        try {
            Thread.sleep(time);
        } catch (Exception ignored) {
        }

        thread.interrupt();

        System.out.println("Pi Greco calcolato: " + piCalc.getPi());

    }
}
