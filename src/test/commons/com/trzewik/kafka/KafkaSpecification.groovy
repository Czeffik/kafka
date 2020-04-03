package com.trzewik.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@EmbeddedKafka(
    topics = [
        '${topic.translated:translated}', '${topic.information:information}'
    ],
    partitions = 4
)
@TestPropertySource(
    properties = ['bootstrap.address=${spring.embedded.kafka.brokers}']
)
class KafkaSpecification extends Specification {
    @Value('${spring.embedded.kafka.brokers}')
    String brokers
}
