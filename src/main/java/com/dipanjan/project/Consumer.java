package com.dipanjan.project;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.Metric;
import org.apache.kafka.common.MetricName;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consumer implements Runnable {

    private final KafkaConsumer consumer;
    private final Logger logger = LoggerFactory.getLogger(Consumer.class.getName());

    public Consumer()
    {
        // new consumer instance with properties
        this.consumer = new KafkaConsumer(getConsumerProperties());
        consumer.subscribe(Arrays.asList(ProducerConsumerConfig.getTopicName()));
    }

    /**
     *
     * @return consumer properties
     */
    private Properties getConsumerProperties()
    {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, ProducerConsumerConfig.getBootStrapServer());
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,ProducerConsumerConfig.getAutoOffsetReset());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ProducerConsumerConfig.getGroupId());
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,ProducerConsumerConfig.getEnableAutoCommit());
        properties.setProperty(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,ProducerConsumerConfig.getAutoCommitInterval_MS());

        return properties;

    }

    @Override
    public void run()
    {
        while(true){
            ConsumerRecords<String,String> consumerRecords = consumer.poll(Duration.ofSeconds(1));

            for(ConsumerRecord<String, String> record : consumerRecords){

                logger.info("Receiving msg with key :" + record.key() + " and value : " +record.value() + "in partition:" +record.partition()
                + " for topic :" +record.topic());

            }
            // see if you are able to pull metrics from the producer
            // need to  decide which to report for the progress monitor
//            consumer.metrics().forEach((key, value) -> {
//                MetricName metricName  = (MetricName) key;
//                logger.info("Consumer Metric Name is --->" + metricName.name());
//
//                Metric metricValue = (Metric) value;
//                logger.info("Consumer Metric Value is --->" + metricValue.metricValue());
//            });
        }

    }


}
