package com.dipanjan.project;


import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The publisher group controls the number of the publisher threads
 * to be started and running based on user request
 */

public class PublisherGroup {

    private final Integer numberOfPublisher;
    private final Integer monitorDisplayConfigInterval;
    private final Integer configurableMean;

    public PublisherGroup(Integer numberOfPublisher, Integer monitorDisplayConfigInterval, Integer configurableMean) {
        this.numberOfPublisher = numberOfPublisher;
        this.monitorDisplayConfigInterval = monitorDisplayConfigInterval;
        this.configurableMean = configurableMean;

    }


    public void executeConsumer() {
        for (int i = 0; i < numberOfPublisher; i++) {
            ConsumerPublisher publisher = new ConsumerPublisher(configurableMean);
            Thread publisherThread = new Thread(publisher);
            publisherThread.start();
            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(publisher.displayMonitor, 0, monitorDisplayConfigInterval, TimeUnit.SECONDS);
        }
    }
}
