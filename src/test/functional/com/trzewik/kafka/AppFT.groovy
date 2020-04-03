package com.trzewik.kafka

import groovy.util.logging.Slf4j
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(['test'])
@SpringBootTest(
    classes = [App],
    properties = [
        'topic.translated=TopicAppTranslated',
        'topic.information=TopicAppInformation',
        'group.id=GroupIdAppFT'
    ]
)
@DirtiesContext
@Slf4j
class AppFT extends KafkaSpecification {
    @Value('${topic.information}')
    String informationTopic

    @Value('${topic.translated}')
    String translatedTopic

    KafkaTestHelper<String> informationTopicHelper
    KafkaTestHelper<String> translatedTopicHelper

    def setup() {
        informationTopicHelper = KafkaTestHelperFactory.create(new KafkaTestHelperFactory.Builder(
            topic: informationTopic,
            brokers: brokers,
            serializer: new StringSerializer(),
            deserializer: new StringDeserializer()
        ))

        translatedTopicHelper = KafkaTestHelperFactory.create(new KafkaTestHelperFactory.Builder(
            topic: translatedTopic,
            brokers: brokers,
            serializer: new StringSerializer(),
            deserializer: new StringDeserializer()
        ))
    }

    def cleanup() {
        informationTopicHelper.close()
        translatedTopicHelper.close()
    }

    def '''should consume message from information topic
            and translate consumed information
            and publish on translated topic
        '''() {
        given:
            String key = 'new key for this topic'
            String value = '{"name":"other name than","description":"super description"}'
        when:
            informationTopicHelper.sendMessageAndWaitForAppear(key, value)
        then:
            translatedTopicHelper.waitForMessage(key, '{"name":"other name than","description":"Translated description"}')
    }
}
