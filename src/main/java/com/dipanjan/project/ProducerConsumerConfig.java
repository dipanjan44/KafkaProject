package com.dipanjan.project;

public class ProducerConsumerConfig {

    private static final String bootStrapServer = "localhost:9092";
    private static final String topicName = "message_1000";

    //Producer Properties
    private static final String writeAcknowledge = "all";
    private static final Integer retries = 0;
    private static final Integer batchSize = 16384;
    private static final Integer waitTimeinMS = 0;
    private final String smsOutTopicName = "sms_1000";

    // Consumer properties
    private static final String groupId = "message-consumer";
    private static final String enableAutoCommit = "true";
    private static final String autoCommitInterval_MS = "1000";
    private static final String autoOffsetReset = "earliest";

    public String getSmsOutTopicName() {
        return smsOutTopicName;
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


    public static String getWriteAcknowledge() {
        return writeAcknowledge;
    }

    public static Integer getRetries() {
        return retries;
    }

    public static Integer getBatchSize() {
        return batchSize;
    }

    public static Integer getWaitTimeinMS() {
        return waitTimeinMS;
    }



    public static String getBootStrapServer() {
        return bootStrapServer;
    }

    public static String getTopicName() {
        return topicName;
    }

    public static String getDefaultMsgCount() {
        return defaultMsgCount;
    }

    private static final String defaultMsgCount = "1000";


}
