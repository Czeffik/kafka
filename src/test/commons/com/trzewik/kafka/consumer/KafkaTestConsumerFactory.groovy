package com.trzewik.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.StringDeserializer

class KafkaTestConsumerFactory {
    final static String GROUP_ID_VALUE = 'TestGroupId'

    private KafkaTestConsumerFactory() {}

    static <V> KafkaTestConsumer<V> create(Builder<V> builder) {
        return new KafkaTestConsumer<V>(
            createConsumer(builder.brokers, builder.deserializer),
            builder.topic
        )
    }

    private static <V> KafkaConsumer<String, V> createConsumer(String brokers, Deserializer<V> deserializer) {
        return new KafkaConsumer<String, V>(kafkaConsumerProperties(brokers, deserializer))
    }

    private static <V> Properties kafkaConsumerProperties(String brokers, Deserializer<V> deserializer) {
        Properties props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID_VALUE)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, 'false')
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.name().toLowerCase())
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer.class)
        return props
    }

    static class Builder<V> {
        String brokers
        Deserializer<V> deserializer
        String topic
    }
}
