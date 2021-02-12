package com.dipanjan.project;


import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer {

    public static void main(String[] args) {

        System.out.println("Welcome to the DP project");

        String kafkaPort = "localhost:9092";
        String topicName ="message_1000";


        // properties to connect to Kafka
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaPort);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // kafka producer instance
        KafkaProducer<String, String> kProducer= new KafkaProducer<String,String>(properties);

        // records to be sent - try with one record and see in topic

        ProducerRecord<String,String> msgRecord= new ProducerRecord<String,String>(topicName,"is it going");

        // send the record

        kProducer.send(msgRecord);

        //flush and close
        kProducer.flush();
        kProducer.close();

    }
}
