package com.dipanjan.project;


public class ConsumerGroup {

    private final Integer numberOfConsumer;
    private final Integer pollingDuration;

    public ConsumerGroup(Integer numberOfConsumer, Integer pollingDuration) {
        this.numberOfConsumer = numberOfConsumer;
        this.pollingDuration = pollingDuration;

    }

    public void executeConsumer(){
        for(int i=0; i< numberOfConsumer; i++)
        {
            ConsumerPublisher consumer = new ConsumerPublisher(pollingDuration);
            Thread consumerThread = new Thread(consumer);
            consumerThread.start();
        }
    }
}
