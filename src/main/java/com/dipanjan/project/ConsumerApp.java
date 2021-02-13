package com.dipanjan.project;

import java.util.Optional;
import java.util.Scanner;

public class ConsumerApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of consumers you want to start ");
        Integer numberOfConsumer = sc.nextInt();
        ConsumerGroup consumerGroup = new ConsumerGroup(numberOfConsumer);
        consumerGroup.executeConsumer();

    }
}