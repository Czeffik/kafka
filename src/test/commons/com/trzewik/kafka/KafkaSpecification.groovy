package com.trzewik.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.time.Duration

@EmbeddedKafka(
    topics = [
        '${topic.translated:translated}', '${topic.information:information}'
    ],
    partitions = 4
)
@TestPropertySource(
    properties = ['bootstrap.address=${spring.embedded.kafka.brokers}']
)
class KafkaSpecification extends Specification implements ConsumingFromKafka, ProducingToKafka {
    final static Duration DEFAULT_DURATION = Duration.ofSeconds(4)

    @Value('${spring.embedded.kafka.brokers}')
    String brokers

    @Override
    Duration getDefaultDuration() {
        return DEFAULT_DURATION
    }
}
