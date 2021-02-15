package com.dipanjan.project;

import java.util.Scanner;

/**
 * This is the publisher app for sending sms messages
 */
public class PublisherApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of publishers you want to start ");
        Integer numberOfPublisher = sc.nextInt();
        System.out.print("Enter progress monitor configurable wait in seconds ");
        Integer monitorDisplayConfigInterval = sc.nextInt();
        System.out.print("Enter configurable mean for the publisher in seconds ");
        Integer configurableMean = sc.nextInt();
        PublisherGroup publisherGroup = new PublisherGroup(numberOfPublisher, monitorDisplayConfigInterval, configurableMean);
        publisherGroup.executeConsumer();
    }
}