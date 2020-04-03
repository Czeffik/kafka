package com.trzewik.kafka.consumer

import org.apache.kafka.clients.consumer.ConsumerRecord

class KafkaMessage<V> {
    final String key
    final V value

    KafkaMessage(String key, V value) {
        this.key = key
        this.value = value
    }

    KafkaMessage(ConsumerRecord<String, V> consumerRecord) {
        key = consumerRecord.key()
        value = consumerRecord.value()
    }
}
