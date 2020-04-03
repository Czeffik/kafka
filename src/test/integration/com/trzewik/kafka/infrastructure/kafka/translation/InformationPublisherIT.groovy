package com.trzewik.kafka.infrastructure.kafka.translation

import com.trzewik.kafka.KafkaSpecification
import com.trzewik.kafka.KafkaTestHelper
import com.trzewik.kafka.KafkaTestHelperFactory
import com.trzewik.kafka.consumer.KafkaMessage
import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.InformationPublisher
import groovy.util.logging.Slf4j
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DirtiesContext
@ContextConfiguration(classes = TranslationPublisherConfiguration)
@TestPropertySource(
    properties = [
        'topic.translated=TopicInformationPublisherIT'
    ]
)
@Slf4j
class InformationPublisherIT extends KafkaSpecification {
    @Autowired
    InformationPublisher publisher

    @Value('${topic.translated}')
    String translatedTopic

    KafkaTestHelper<String> helper

    def setup() {
        helper = KafkaTestHelperFactory.create(new KafkaTestHelperFactory.Builder(
            topic: translatedTopic,
            brokers: brokers,
            serializer: new StringSerializer(),
            deserializer: new StringDeserializer()
        ))
    }

    def cleanup() {
        helper.close()
    }

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
            List<KafkaMessage<String>> consumed = helper.consumeExpectedNumberOfMessages(send.size())
            consumed.each {
                assert send.any { k, v -> it.key == k }
                assert send.any { k, v -> it.value.contains(v.name) && it.value.contains(v.description) }
            }
    }
}
