package com.trzewik.kafka.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.Serializer
import org.apache.kafka.common.serialization.StringSerializer

class KafkaTestProducerFactory {
    private KafkaTestProducerFactory() {}

    static <V> KafkaTestProducer<V> create(Builder<V> builder) {
        return new KafkaTestProducer<V>(
            createProducer(builder.brokers, builder.serializer),
            builder.topic
        )
    }

    private static <V> KafkaProducer<String, String> createProducer(String brokers, Serializer<V> serializer) {
        return new KafkaProducer<String, String>(producerProperties(brokers, serializer))
    }

    private static <V> Properties producerProperties(String brokers, Serializer<V> serializer) {
        Properties props = new Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
        props.put(ProducerConfig.RETRIES_CONFIG, 0)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, serializer.class)
        return props
    }

    static class Builder<V> {
        String brokers
        Serializer<V> serializer
        String topic
    }
}
