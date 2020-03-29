package com.trzewik.kafka.interfaces.kafka

import com.trzewik.kafka.KafkaSpecification
import com.trzewik.kafka.domain.translation.TranslationService
import com.trzewik.kafka.interfaces.kafka.translation.InformationConsumerConfiguration
import org.awaitility.Awaitility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource

@ActiveProfiles(['test', TestInformationConsumerConfig.PROFILE])
@ContextConfiguration(classes = [
    InformationConsumerConfiguration,
    TestInformationConsumerConfig
])
@TestPropertySource(
    properties = [
        'topic.information=InformationConsumerIT'
    ]
)
class InformationConsumerIT extends KafkaSpecification {
    @Value('${topic.information}')
    String informationTopic

    @Autowired
    TranslationService translationServiceMock

    def 'should consume from information topic and trigger translation service'() {
        given:
            String key = 'example key'
            String name = 'name'
            String description = 'example description'
            String value = "{\"name\":\"${name}\",\"description\":\"${description}\"}"
        when:
            sendMessage(informationTopic, key, value)
        then:
            Awaitility.await().atMost(DEFAULT_DURATION).untilAsserted {
                1 * translationServiceMock.translate(key, {
                    assert it.getName() == name
                    assert it.getDescription() == description
                })
            }
    }
}
