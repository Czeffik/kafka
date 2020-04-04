package com.trzewik.kafka

import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy

@TestConfiguration
class TestKafkaConfig {

    @Lazy
    @Bean(destroyMethod = 'close')
    KafkaTestHelper<String> translatedTopicHelper(TestKafkaProperties properties) {
        return KafkaTestHelperFactory.create(new KafkaTestHelperFactory.Builder(
            topic: properties.topicTranslated,
            brokers: properties.bootstrapAddress,
            serializer: new StringSerializer(),
            deserializer: new StringDeserializer()
        ))
    }

    @Lazy
    @Bean(destroyMethod = 'close')
    KafkaTestHelper<String> informationTopicHelper(TestKafkaProperties properties) {
        return KafkaTestHelperFactory.create(new KafkaTestHelperFactory.Builder(
            topic: properties.topicInformation,
            brokers: properties.bootstrapAddress,
            serializer: new StringSerializer(),
            deserializer: new StringDeserializer()
        ))
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
