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
        props.put("bootstrap.servers", "0.0.0.0:32806, 0.0.0.0:32810, 0.0.0.0:32808");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        String topic = "my-topic";
        int numberOfRecords = 100; // number of records to send

        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props)) {
            for (int i = 0; i < numberOfRecords; i++) {
                String key = String.format("Record-%d", i);
                String message = String.format("Message: %d sent at %s", i, LocalDateTime.now());
                ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, message);
                kafkaProducer.send(record);
                LOGGER.debug("{}", record);

                Thread.sleep(500); // randomize the time between record sends
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
