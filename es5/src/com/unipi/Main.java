package com.unipi;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        String startDir;
        String outputfile;
        int K;
        try{
            startDir = args[0];
            outputfile = args[1];
            K = Integer.parseInt(args[2]);

        }catch (Exception e){
            System.err.println("Valori passati non corretti");
            return;
        }

        SynchronizedList list = new SynchronizedList();
        Printer printer = new Printer(outputfile);

        Producer producer = new Producer(startDir, list);
        Consumer[] consumers = new Consumer[K];

        for (int i = 0; i < K; i++) {
            Consumer consumer = new Consumer(list, printer);
            consumers[i] = consumer;
            consumer.start();
        }

        System.out.println("Avvio in corso...");
        System.out.println();
        Thread.sleep(1000); // do il tempo ai consumatori di avviarsi
        producer.start();

        producer.join();
        for (int i = 0; i < K; i++) {
            consumers[i].join();
        }

        printer.close();
    }
}
