package io.github.aelesbao;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Properties;

public class ProducerApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProducerApp.class);

    public static void main(String[] args) {

        // Create the Properties class to instantiate the Producer with the desired settings:
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "1");
        props.put("buffer.memory", 33554432);
        props.put("compression.type", "none");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("client.id", "");
        props.put("linger.ms", 0);
        props.put("max.block.ms", 60000);
        props.put("max.request.size", 1048576);
        props.put("partitioner.class", "org.apache.kafka.clients.producer.internals.DefaultPartitioner");
        props.put("request.timeout.ms", 30000);
        props.put("timeout.ms", 30000);
        props.put("max.in.flight.requests.per.connection", 5);
        props.put("retry.backoff.ms", 5);

        String topic = "my-topic";
        int numberOfRecords = 100; // number of records to send

        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props)) {
            for (int i = 0; i < numberOfRecords; i++) {
                String key = String.format("Record-%d", i);
                String message = String.format("Message: %d sent at %s", i, LocalDateTime.now());
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);

                kafkaProducer.send(record);
                LOGGER.debug("key={}, message={}", key, message);
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while sending the messages", e);
        }
    }
}
