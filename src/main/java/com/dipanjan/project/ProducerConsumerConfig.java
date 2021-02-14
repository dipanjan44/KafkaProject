package com.dipanjan.project;

public class ProducerConsumerConfig {

    private static final String bootStrapServer = "localhost:9092";
    private static final String topicName = "message_1000";
    private static final String defaultMsgCount = "1000";

    //Producer Properties
    private static final String writeAcknowledge = "all";
    private static final Integer retries = 0;
    private static final Integer batchSize = 16384;



    // ConsumerPublisher properties
    private static final String groupId = "message-consumer";
    private static final String enableAutoCommit = "true";
    private static final String autoCommitInterval_MS = "1000";
    private static final String autoOffsetReset = "earliest";

    //Publisher properties
    private static final String outTopicName = "sms_1000";

    public static String getBootStrapServer() {
        return bootStrapServer;
    }

    public static String getTopicName() {
        return topicName;
    }

    public static String getDefaultMsgCount() {
        return defaultMsgCount;
    }

    public static String getWriteAcknowledge() {
        return writeAcknowledge;
    }

    public static Integer getRetries() {
        return retries;
    }

    public static Integer getBatchSize() {
        return batchSize;
    }

    public static String getGroupId() {
        return groupId;
    }

    public static String getEnableAutoCommit() {
        return enableAutoCommit;
    }

    public static String getAutoCommitInterval_MS() {
        return autoCommitInterval_MS;
    }

    public static String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public static String getOutTopicName() {
        return outTopicName;
    }

    public static String getWaitTimeinMS() {
        return waitTimeinMS;
    }

    // configurable parameter for publisher to wait
    private static final String waitTimeinMS = "1000";


}
