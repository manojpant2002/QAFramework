package com.lseg.digital.framework.qa.util;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import java.util.Properties;
import java.util.Collections;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.List;
import java.util.ArrayList;

public class KafkaClient {
    private final Producer<String, String> producer;
    private final Consumer<String, String> consumer;
    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    public KafkaClient(String bootstrapServers) {
        Properties prodProps = new Properties();
        prodProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        prodProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        prodProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producer = new KafkaProducer<>(prodProps);

        Properties consProps = new Properties();
        consProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        consProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumer = new KafkaConsumer<>(consProps);
    }

    public void produce(String topic, String key, String value) {
        producer.send(new ProducerRecord<>(topic, key, value));
    }

    public void produce(String topic, String value) {
        producer.send(new ProducerRecord<>(topic, value));
    }

    public ConsumerRecords<String, String> consume(String topic, int timeoutMs) {
        consumer.subscribe(Collections.singleton(topic));
        return consumer.poll(Duration.ofMillis(timeoutMs));
    }

    public Future<RecordMetadata> sendMessageAsync(String topic, String key, String value) {
        return executor.submit(() -> producer.send(
            new ProducerRecord<>(topic, key, value)).get());
    }
    
    public Future<List<String>> consumeMessagesAsync(String topic, int maxMessages) {
        return executor.submit(() -> {
            List<String> messages = new ArrayList<>();
            // ... consumption logic
            return messages;
        });
    }
}