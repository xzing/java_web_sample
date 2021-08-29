package com.zing.demo001_kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * create at 2021-08-29 22:09
 *
 * @author Zing
 **/

public class KafkaClientTest {
    public static final String brokerList = "localhost:9092";
    public static final String topic = "zing-test";

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.RETRIES_CONFIG, 2);
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("bootstrap.servrs", brokerList);
        try (KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "kafka-demo", "hello kafka");
            producer.send(record);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
