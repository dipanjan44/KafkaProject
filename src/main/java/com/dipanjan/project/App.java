package com.dipanjan.project;

import java.util.Optional;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of messages you want to generate ");
        Optional<String> numMessages = Optional.of(sc.nextLine());
        MessageProducer producer = new MessageProducer(numMessages);
        Thread producerThread = new Thread(producer);
        producerThread.start();
    }
}

