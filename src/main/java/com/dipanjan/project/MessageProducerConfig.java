package com.dipanjan.project;

public class MessageProducerConfig {

        private static final String bootStrapServer = "localhost:9092";
        private static final String topicName ="message_1000";

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
