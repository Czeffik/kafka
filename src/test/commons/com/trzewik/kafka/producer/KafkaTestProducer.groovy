package com.trzewik.kafka.producer

import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata

import java.util.concurrent.Future

@Slf4j
class KafkaTestProducer<V> implements AutoCloseable {
    private final KafkaProducer<String, V> producer
    final String topic

    @PackageScope
    KafkaTestProducer(KafkaProducer<String, V> producer, String topic) {
        this.producer = producer
        this.topic = topic
    }

    void send(String key, V value) {
        sendAsync(key, value).get()
    }

    void send(ProducerRecord<String, V> record) {
        sendAsync(record).get()
    }

    Future<RecordMetadata> sendAsync(String key, V value) {
        return sendAsync(createRecord(key, value))
    }

    Future<RecordMetadata> sendAsync(ProducerRecord<String, V> record) {
        log.info('Sending message with key: [{}], value: [{}] to topic: [{}]', record.key(), record.value(), topic)
        return producer.send(record)
    }

    ProducerRecord<String, V> createRecord(String key, V value) {
        return new ProducerRecord(topic, key, value)
    }

    @Override
    void close() throws Exception {
        producer.close()
    }
}
