package com.trzewik.kafka

import com.trzewik.kafka.consumer.KafkaMessage
import com.trzewik.kafka.consumer.KafkaTestConsumer
import com.trzewik.kafka.producer.KafkaTestProducer
import groovy.transform.PackageScope
import org.awaitility.Awaitility

import java.time.Duration

class KafkaTestHelper<V> implements AutoCloseable {
    final KafkaTestProducer<V> producer
    final KafkaTestConsumer<V> consumer
    final Duration duration

    @PackageScope
    KafkaTestHelper(KafkaTestProducer<V> producer, KafkaTestConsumer<V> consumer, long timeout = 2) {
        this.producer = producer
        this.consumer = consumer
        this.duration = Duration.ofSeconds(timeout)
    }

    void sendMessagesAndWaitForAppear(Map<String, V> messages) {
        messages.each { k, v -> producer.send(k, v) }
        waitForMessagesAppear(messages)
    }

    void waitForMessagesAppear(Map<String, V> messages) {
        List<KafkaMessage<V>> consumed = []
        Awaitility.await().atMost(duration).untilAsserted {
            consumed.addAll(consumer.consumeAll())
            messages.each { k, v ->
                assert consumed.any { it.key == k }
                assert consumed.any { it.value == v }
            }
        }
    }

    void sendMessageAndWaitForAppear(String key, V value) {
        producer.send(key, value)
        waitForMessage(key, value)
    }

    void waitForMessage(String key, V value) {
        Awaitility.await().atMost(duration).untilAsserted {
            def messages = consumer.consumeAll()
            assert messages.any { it.key == key }
            assert messages.any { it.value == value }
        }
    }

    List<KafkaMessage<V>> consumeExpectedNumberOfMessages(int expectedNumberOfMessages = 1) {
        List<KafkaMessage<V>> messages = []
        Awaitility.await().atMost(duration).untilAsserted {
            messages.addAll(consumer.consumeAll())
            assert messages.size() == expectedNumberOfMessages
        }
        return messages
    }

    @Override
    void close() throws Exception {
        producer.close()
        consumer.close()
    }
}
