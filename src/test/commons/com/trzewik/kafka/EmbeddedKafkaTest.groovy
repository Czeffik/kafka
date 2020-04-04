package com.trzewik.kafka

import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.TestPropertySource

import java.lang.annotation.ElementType
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.lang.annotation.Target

@DirtiesContext
@EmbeddedKafka(
    topics = [
        '${kafka.topic.translated:translated}', '${kafka.topic.information:information}'
    ],
    partitions = 4
)
@TestPropertySource(
    properties = ['kafka.bootstrap.address=${spring.embedded.kafka.brokers}']
)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface EmbeddedKafkaTest {

}
