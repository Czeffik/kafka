package com.trzewik.kafka

import com.trzewik.kafka.consumer.KafkaConsumerFactory
import com.trzewik.kafka.producer.KafkaProducerFactory
import io.github.czeffik.kafka.test.clients.consumer.KafkaTestConsumer
import io.github.czeffik.kafka.test.clients.consumer.KafkaTestConsumerFactory
import io.github.czeffik.kafka.test.clients.helper.KafkaTestHelper
import io.github.czeffik.kafka.test.clients.helper.KafkaTestHelperFactory
import io.github.czeffik.kafka.test.clients.producer.KafkaTestProducer
import io.github.czeffik.kafka.test.clients.producer.KafkaTestProducerFactory
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy

import java.time.Duration

@TestConfiguration
class TestKafkaConfig {

    @Lazy
    @Bean(destroyMethod = 'close')
    KafkaTestHelper<String> translatedTopicHelper(TestKafkaProperties properties){
        KafkaTestConsumer<String> consumer = KafkaTestConsumerFactory.createConsumer(
            KafkaConsumerFactory.createConsumer(properties.bootstrapAddress, new StringDeserializer()),
            properties.topicTranslated,
            Duration.ofSeconds(2)
        )
        KafkaTestProducer<String> producer = KafkaTestProducerFactory.createProducer(
            KafkaProducerFactory.createProducer(properties.bootstrapAddress, new StringSerializer()),
            properties.topicTranslated
        )
        return KafkaTestHelperFactory.createHelper(producer, consumer, Duration.ofSeconds(2))
    }
    @Lazy
    @Bean(destroyMethod = 'close')
    KafkaTestHelper<String> informationTopicHelper(TestKafkaProperties properties) {
        KafkaTestConsumer<String> consumer = KafkaTestConsumerFactory.createConsumer(
            KafkaConsumerFactory.createConsumer(properties.bootstrapAddress, new StringDeserializer()),
            properties.topicInformation,
            Duration.ofSeconds(2)
        )
        KafkaTestProducer<String> producer = KafkaTestProducerFactory.createProducer(
            KafkaProducerFactory.createProducer(properties.bootstrapAddress, new StringSerializer()),
            properties.topicInformation
        )
        return KafkaTestHelperFactory.createHelper(producer, consumer, Duration.ofSeconds(2))
    }

    @Bean
    TestKafkaProperties properties(
        @Value('${kafka.topic.information}') information,
        @Value('${kafka.topic.translated}') translated,
        @Value('${kafka.bootstrap.address}') bootstrapAddress
    ) {
        return new TestKafkaProperties(
            topicInformation: information,
            topicTranslated: translated,
            bootstrapAddress: bootstrapAddress
        )
    }
}
