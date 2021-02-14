package com.dipanjan.project;

import java.time.Duration;
import java.util.Arrays;
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
    private final Logger logger = LoggerFactory.getLogger(ConsumerPublisher.class.getName());
    private final Integer pollingDuration;

    public ConsumerPublisher(Integer pollingDuration) {
        // new consumer instance with properties
        this.consumer = new KafkaConsumer(getConsumerProperties());
        this.publisher = new KafkaProducer(getPublisherProperties());
        consumer.subscribe(Arrays.asList(ProducerConsumerConfig.getTopicName()));

        this.pollingDuration = pollingDuration;
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
        properties.setProperty(ConsumerConfig.METRICS_SAMPLE_WINDOW_MS_CONFIG, "5000");


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
        properties.setProperty(ProducerConfig.LINGER_MS_CONFIG, ProducerConsumerConfig.getWaitTimeinMS());

        return properties;
    }

    @Override
    public void run() {
        while (true) {
            ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofSeconds(pollingDuration));

            for (ConsumerRecord<String, String> record : consumerRecords) {

                logger.info(
                        "Received msg with key :" + record.key() + " and value : " + record.value() + "in partition:" + record
                                .partition() + " for topic :" + record.topic() + "running on thread: " + Thread.currentThread()
                                .getName());

                ProducerRecord publisherRecord =
                        new ProducerRecord(ProducerConsumerConfig.getOutTopicName(), record.key(), record.value());
                publisher.send(publisherRecord, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception e) {

                        logger.info("Publisher record pushed to: " + " Topic: " + recordMetadata.topic() + " to partition: "
                                + recordMetadata.partition() + "running on thread: " + Thread.currentThread().getName());

                    }
                });

            }
            // see if you are able to pull metrics from the producer
            // need to  decide which to report for the progress monitor
            consumer.metrics().forEach((key, value) -> {
                MetricName metricName = (MetricName) key;
                logger.info("ConsumerPublisher Metric Name is --->" + metricName.name());

                Metric metricValue = (Metric) value;
                logger.info("ConsumerPublisher Metric Value is --->" + metricValue.metricValue());
            });


        }

    }



}
