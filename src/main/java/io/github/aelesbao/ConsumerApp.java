package io.github.aelesbao;

import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerApp.class);

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("fetch.min.bytes", 1);
        props.put("client.id", "");
        props.put("group.id", "my-consumer");
        props.put("heartbeat.interval.ms", 3000);
        props.put("max.partition.fetch.bytes", 1048576);
        props.put("session.timeout.ms", 30000);
        props.put("connections.max.idle.ms", 540000);
        props.put("auto.offset.reset", "latest");
        props.put("enable.auto.commit", true);
        props.put("auto.commit.interval.ms", 5000);
        props.put("exclude.internal.topics", true);
        props.put("max.poll.records", 2147483647);
        props.put("partition.assignment.strategy", "org.apache.kafka.clients.consumer.RangeAssignor");
        props.put("request.timeout.ms", 40000);
        props.put("fetch.max.wait.ms", 500);
        props.put("metadata.max.age.ms", 300000);
        props.put("reconnect.backoff.ms", 50);
        props.put("retry.backoff.ms", 100);

        try (KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer(props)) {
            kafkaConsumer.subscribe(Arrays.asList("my-topic"));

            while (true) {
                ConsumerRecords<String, String> records = kafkaConsumer.poll(1000);
                if (!records.isEmpty()) {
                    LocalDateTime now = LocalDateTime.now();
                    records.forEach(record -> LOGGER.info("Received at {} record={}", now, record));
                    kafkaConsumer.commitSync();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to consume records", e);
        }
    }
}
