package com.dipanjan.project;

import java.time.Duration;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Properties;



import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerPublisher implements Runnable {

    private final KafkaConsumer consumer;
    private final KafkaProducer publisher;
    private final String configurableMean;
    private final Logger logger = LoggerFactory.getLogger(ConsumerPublisher.class.getName());
    private final String errorTopicName;
    private final Integer errorRate;


    public ConsumerPublisher(Integer configurableMean, Integer errorRate) {
        // new consumer instance with properties
        this.configurableMean = Utils.getConfigurableMean(configurableMean);
        this.errorTopicName = ProducerConsumerConfig.getErrorTopicName();
        this.consumer = new KafkaConsumer(getConsumerProperties());
        this.publisher = new KafkaProducer(getPublisherProperties());
        this.errorRate = errorRate;
        consumer.subscribe(Arrays.asList(ProducerConsumerConfig.getTopicName()));

    }

    /**
     * @return consumer properties
     */
    private Properties getConsumerProperties() {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ProducerConsumerConfig.getBootStrapServer());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, ProducerConsumerConfig.getAutoOffsetReset());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ProducerConsumerConfig.getGroupId());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, ProducerConsumerConfig.getEnableAutoCommit());
        properties
                .setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, ProducerConsumerConfig.getAutoCommitInterval_MS());


        return properties;

    }

    /**
     * @return publisher properties
     */
    private Properties getPublisherProperties() {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, ProducerConsumerConfig.getBootStrapServer());
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.ACKS_CONFIG, ProducerConsumerConfig.getWriteAcknowledge());
        // waiting a random period time for the publisher
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, configurableMean);


        return properties;
    }

    @Override
    public void run() {
        while (true) {

            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

            Iterator<ConsumerRecord<String, String>> recordIterator = consumerRecords.iterator();

            logger.info("Number of message produced which can be published ->  " + consumerRecords.count());
            int recordCount = consumerRecords.count();

            int noOfMessagetoPublish = ((100-errorRate) * recordCount) / 100;

            logger.info("Number of message to be published after applying error rate -> " + noOfMessagetoPublish);

            for (int i = 0; i < noOfMessagetoPublish; i++) {
                if (recordIterator.hasNext()) {
                    ConsumerRecord<String, String> record = recordIterator.next();
                    logger.info("Received msg with key :" + record.key() + " and value : " + record.value() + "in partition:"
                            + record.partition() + " for topic :" + record.topic() + "running on thread: " + Thread
                            .currentThread().getName());

                    ProducerRecord publisherRecord =
                            new ProducerRecord(ProducerConsumerConfig.getOutTopicName(), record.key(), record.value());

                    publisher.send(publisherRecord, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                            if (null == e) {
                                logger.info(
                                        "Publisher record pushed to: " + " Topic: " + recordMetadata.topic() + " to partition: "
                                                + recordMetadata.partition() + "running on thread: " + Thread.currentThread()
                                                .getName());

                            } else {

                                publisher.send(new ProducerRecord(ProducerConsumerConfig.getErrorTopicName(), record.key(),
                                        record.value()));

                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * Got this idea here : https://stackoverflow.com/questions/12908412/print-hello-world-every-x-seconds
     * Configurable display monitor to show metrics at the desired interval
     */

    // this will give metrics per publisher
    // TODO: need to aggegrate the metrics over all the publishers:
    // best solution would be to use datadog or sumologic
    // currently see the aggregated metrics in the http://localhost:9021/clusters/
    Runnable displayMonitor = new Runnable() {
        @Override
        public void run() {
            publisher.metrics().forEach((key, value) -> {
                MetricName metricName = (MetricName) key;
                Metric metricValue = (Metric) value;
                //Average number of requests sent per second
                if (metricName.name().equals("request-rate")) {
                    logger.info("Metric Name is --->" + metricName.name());
                    logger.info("Metric Value is --->" + metricValue.metricValue());
                }

            });
        }
    };

}

