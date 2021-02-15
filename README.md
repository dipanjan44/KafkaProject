**Installation requirements on local system:**

`JDK 8`

`kafka`

**If you have a MacOs, you can do a brew install instead of a manual installation:**  
                              
    brew install kafka # this will install java 1.8, zookeeper, and kafka  

**How to run the code:**

1. Clone the repo from here: https://github.com/dipanjan44/KafkaProject.git

2. Open the project in a IDE of your choice (Intellij/Ecllipse)

3. Open a terminal and run `docker-compose -f docker-compose-kafka-setup.yml up`     # will bring up a kafka cluster locally

4. Open a new terminal and run the following command to create topic for the producer to write to:

      `kafka-topics --zookeeper localhost:2181 --create --topic message_1000 --partitions 3 --replication-factor 1`


5. Open a new terminal and execute the below command to see the messages been published by the publisher:

      `kafka-console-consumer --bootstrap-server localhost:9092 --topic sms_1000  --max-messages 10000`

6.Run the following classes from your IDE:

      `PublisherApp.java (Provide the configurations of your choice. All are mandatory)`

7. Run the ProducerApp.java once the above app has started

      `ProducerApp.java (Provide the number of messages of your choice. If you dont want to provide, just hit enter and it will default to 1000)`

8. Navigate to `http://localhost:9021/clusters/` to see the topics and metrics


**Note:** 
  The publisherApp will be running until you stop it explicitly. But the producer app will shut down after it generates 
  the requested number of messages. If you want to produce more messages you can repeat step 7 and observe messeges flowing again 
  the terminal opened in step 5

  The publisher metrics per publisher will be reported in the console and the aggregated metrics are visible in control center 
  as defined in step 8

