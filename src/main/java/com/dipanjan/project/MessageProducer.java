package com.dipanjan.project;


import java.util.Optional;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    public static void produceMessages(Optional<String> numberOfMessages) {

        String msgCount =
                numberOfMessages.get().isEmpty() ? MessageProducerConfig.getDefaultMsgCount() : numberOfMessages.get();

        logger.info("The message count from user is " + msgCount);

        // properties to connect to Kafka
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MessageProducerConfig.getBootStrapServer());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // kafka producer instance
        KafkaProducer<String, String> kProducer = new KafkaProducer<String, String>(properties);

        // records to be sent - try with one record and see in topic
        for (int loopCounter = 0; loopCounter < Integer.parseInt(msgCount); loopCounter++) {

            String genaratedMessage = Utils.generateRandomString();
            String msgKey = "key_" + genaratedMessage;
            String msgValue = "value_" + genaratedMessage;
            ProducerRecord<String, String> msgRecord =
                    new ProducerRecord<String, String>(MessageProducerConfig.getTopicName(), msgKey, msgValue);

            // send the record
            int finalLoopCounter = loopCounter;
            kProducer.send(msgRecord, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                    logger.info("New Data pushed to " + " Topic" + recordMetadata.topic() + " to partition" + recordMetadata
                            .partition() + " _msg number" + finalLoopCounter);

                }
            });
        }
        //flush and close
        kProducer.flush();
        kProducer.close();
    }
}
