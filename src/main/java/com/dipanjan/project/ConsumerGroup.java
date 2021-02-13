package com.dipanjan.project;


public class ConsumerGroup {

    private final Integer numberOfConsumer;

    public ConsumerGroup(Integer numberOfConsumer) {
        this.numberOfConsumer = numberOfConsumer;

    }

    public void executeConsumer(){
        for(int i=0; i< numberOfConsumer; i++)
        {
            Consumer consumer = new Consumer();
            Thread consumerThread = new Thread(consumer);
            consumerThread.start();
        }
    }
}
