package com.dipanjan.project;

import java.util.Optional;
import java.util.Scanner;

/**
 * This is the producer app to produce configurable number of messages
 */
public class ProducerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of messages you want to generate ");
        Optional<String> numMessages = Optional.of(sc.nextLine());
        Producer producer = new Producer(numMessages);
        Thread producerThread = new Thread(producer);
        producerThread.start();

    }
}