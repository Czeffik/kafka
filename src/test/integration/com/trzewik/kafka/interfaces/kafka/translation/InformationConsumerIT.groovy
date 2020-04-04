package com.trzewik.kafka.interfaces.kafka.translation

import com.trzewik.kafka.EmbeddedKafkaTest
import com.trzewik.kafka.KafkaTestHelper
import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.TranslationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ActiveProfiles(['test', TestInformationConsumerConfig.PROFILE])
@ContextConfiguration(classes = [
    InformationConsumerConfiguration,
    TestInformationConsumerConfig
]
)
@TestPropertySource(
    properties = [
        'kafka.topic.information=TopicInformationConsumerIT',
        'kafka.group.id=GroupIdInformationConsumerIT'
    ]
)
@EmbeddedKafkaTest
class InformationConsumerIT extends Specification {
    @Autowired
    TranslationService translationServiceMock

    @Autowired
    KafkaTestHelper<String> informationTopicHelper

    def 'should consume 215 messages from information topic and trigger translation service'() {
        given:
            Map<String, String> messages = [:]
            215.times {
                String description = 'example description' + it
                String value = "{\"name\":\"name\",\"description\":\"${description}\"}"
                messages.put('example key' + it, value)
            }
        when:
            informationTopicHelper.sendMessagesAndWaitForAppear(messages)
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
