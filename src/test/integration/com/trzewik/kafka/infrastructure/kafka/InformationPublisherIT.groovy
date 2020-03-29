package com.trzewik.kafka.infrastructure.kafka

import com.trzewik.kafka.KafkaSpecification
import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.InformationPublisher
import com.trzewik.kafka.infrastructure.kafka.translation.TranslationPublisherConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@DirtiesContext
@ContextConfiguration(classes = TranslationPublisherConfiguration)
@TestPropertySource(
    properties = [
        'topic.translated=InformationPublisherIT'
    ]
)
class InformationPublisherIT extends KafkaSpecification {
    @Autowired
    InformationPublisher publisher

    @Value('${topic.translated}')
    String translatedTopic

    def 'should publish information on topic successfully'() {
        given:
            def key = 'test-key'
            def information = new Information('name', 'description')
        when:
            publisher.publish(key, information)
        then:
            def messages = consumeAllFrom(translatedTopic, 1)
            with(messages.first()) {
                assert it.key == key
                assert it.value == '{"name":"name","description":"description"}'
            }

    }
}
