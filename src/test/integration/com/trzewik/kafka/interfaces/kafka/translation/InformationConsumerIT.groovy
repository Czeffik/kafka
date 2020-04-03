package com.trzewik.kafka.interfaces.kafka.translation

import com.trzewik.kafka.KafkaSpecification
import com.trzewik.kafka.KafkaTestHelper
import com.trzewik.kafka.KafkaTestHelperFactory
import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.TranslationService
import groovy.util.logging.Slf4j
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ActiveProfiles(['test', TestInformationConsumerConfig.PROFILE])
@DirtiesContext
@ContextConfiguration(classes = [
    InformationConsumerConfiguration,
    TestInformationConsumerConfig
]
)
@TestPropertySource(
    properties = [
        'topic.information=TopicInformationConsumerIT',
        'group.id=GroupIdInformationConsumerIT'
    ]
)
@Slf4j
class InformationConsumerIT extends KafkaSpecification {
    @Value('${topic.information}')
    String informationTopic

    @Autowired
    TranslationService translationServiceMock

    KafkaTestHelper<String> helper

    def setup() {
        helper = KafkaTestHelperFactory.create(new KafkaTestHelperFactory.Builder(
            topic: informationTopic,
            brokers: brokers,
            serializer: new StringSerializer(),
            deserializer: new StringDeserializer()
        ))
    }

    def cleanup() {
        helper.close()
    }

    def 'should consume 215 messages from information topic and trigger translation service'() {
        given:
            Map<String, String> messages = [:]
            215.times {
                String description = 'example description' + it
                String value = "{\"name\":\"name\",\"description\":\"${description}\"}"
                messages.put('example key' + it, value)
            }
        when:
            helper.sendMessagesAndWaitForAppear(messages)
        then:
            messages.each { k, v ->
                1 * translationServiceMock.translate(k, {
                    Information i ->
                        assert v.contains(i.getName())
                        assert v.contains(i.getDescription())
                })
            }
    }
}
