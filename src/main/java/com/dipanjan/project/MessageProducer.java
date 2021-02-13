package com.dipanjan.project;


import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.serialization.StringSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageProducer implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    private final String msgCount;
    private final KafkaProducer kProducer;
    private final Properties kProducerProperties;

    public MessageProducer(Optional<String> msgCount) {
        this.msgCount = msgCount.get().isEmpty() ? MessageProducerConfig.getDefaultMsgCount(): msgCount.get();
        this.kProducerProperties = getProducerProperties();
        // kafka producer instance
        kProducer = new KafkaProducer<String, String>(kProducerProperties);
    }

    private Properties getProducerProperties()
    {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, MessageProducerConfig.getBootStrapServer());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        return properties;
    }

    @Override
    public void run() {
        // records to be sent
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

            // see if you are able to pull metrics from the producer
            // need to  decide which to report for the progress monitor
            kProducer.metrics().forEach((metricName, metric) -> {
                MetricName metricName1 = (MetricName) metricName;
                logger.info("Metric Name is --->" +metricName1.name() );

                Metric metric1 = (Metric) metric;
                logger.info("Metric is --->" +metric1.metricValue() );
            });

        }

        //flush and close
        kProducer.flush();
        kProducer.close();
    }

}
