package com.unipi;

public class PiGrecoCalculator implements Runnable{
    private double pi;
    private double accuracy;

    public PiGrecoCalculator(double accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public void run() {
        double num=4;
        double denum=1;
        int sign=-1;
        pi=num/denum;

        do{
            pi += sign * (num / (denum += 2));
            sign*=-1;
        }while((pi-Math.PI)<accuracy && !Thread.currentThread().isInterrupted());
    }

    public double getPi() {
        return pi;
    }
}
