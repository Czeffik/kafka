package com.trzewik.kafka

import com.trzewik.kafka.consumer.KafkaTestConsumerFactory
import com.trzewik.kafka.producer.KafkaTestProducerFactory
import org.apache.kafka.common.serialization.Deserializer
import org.apache.kafka.common.serialization.Serializer

class KafkaTestHelperFactory {
    private KafkaTestHelperFactory() {}

    static <V> KafkaTestHelper<V> create(Builder<V> builder) {
        return new KafkaTestHelper<V>(
            KafkaTestProducerFactory.create(new KafkaTestProducerFactory.Builder(
                topic: builder.topic,
                brokers: builder.brokers,
                serializer: builder.serializer
            )),
            KafkaTestConsumerFactory.create(new KafkaTestConsumerFactory.Builder(
                topic: builder.topic,
                brokers: builder.brokers,
                deserializer: builder.deserializer
            )),
            builder.timeout
        )
    }

    static class Builder<V> {
        String topic
        String brokers
        Serializer<V> serializer
        Deserializer<V> deserializer
        long timeout = 2
    }
}
