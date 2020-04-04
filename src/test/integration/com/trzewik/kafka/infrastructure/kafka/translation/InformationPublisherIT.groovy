package com.trzewik.kafka.infrastructure.kafka.translation

import com.trzewik.kafka.EmbeddedKafkaTest
import com.trzewik.kafka.KafkaTestHelper
import com.trzewik.kafka.TestKafkaConfig
import com.trzewik.kafka.consumer.KafkaMessage
import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.InformationPublisher
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ActiveProfiles(['test', 'information-producer-test'])
@ContextConfiguration(classes = [TranslationPublisherConfiguration, TestKafkaConfig])
@TestPropertySource(
    properties = [
        'kafka.topic.translated=TopicInformationPublisherIT'
    ]
)
@EmbeddedKafkaTest
class InformationPublisherIT extends Specification {
    @Autowired
    InformationPublisher publisher

    @Autowired
    KafkaTestHelper<String> translatedTopicHelper

    def 'should publish all information on topic successfully'() {
        given:
            Map<String, Information> send = [:]
            142.times {
                def key = 'test-key' + it
                def information = new Information('name', 'description' + it)
                send[key] = information
            }
        when:
            send.each { k, i -> publisher.publish(k, i) }

        then:
            List<KafkaMessage<String>> consumed = translatedTopicHelper.consumeExpectedNumberOfMessages(send.size())
            consumed.each {
                assert send.any { k, v -> it.key == k }
                assert send.any { k, v -> it.value.contains(v.name) && it.value.contains(v.description) }
            }
    }
}
