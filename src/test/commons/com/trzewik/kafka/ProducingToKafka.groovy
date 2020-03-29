package com.trzewik.kafka

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.apache.kafka.common.serialization.StringSerializer

import java.util.concurrent.Future

trait ProducingToKafka {
    abstract String getBrokers()

    void sendMessage(String topicName, String key, String value) {
        def producer = createProducer()

        Future<RecordMetadata> future = producer.send(createRecord(topicName, key, value))
        future.get()

        producer.close()
    }

    private ProducerRecord<String, String> createRecord(String topicName, String key, String value) {
        return new ProducerRecord<String, String>(topicName, key, value)
    }

    private KafkaProducer<String, String> createProducer() {
        return new KafkaProducer<String, String>(producerProperties())
    }

    private Properties producerProperties() {
        Properties props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getBrokers())
        props.put(ProducerConfig.RETRIES_CONFIG, 0)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        return props
    }
}
