package com.trzewik.kafka.interfaces.kafka.translation

import com.trzewik.kafka.KafkaSpecification
import com.trzewik.kafka.domain.translation.TranslationService
import groovy.util.logging.Slf4j
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
])
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

    def 'should consume from information topic and trigger translation service'() {
        given:
            String key = 'example key'
            String name = 'name'
            String description = 'example description'
            String value = "{\"name\":\"${name}\",\"description\":\"${description}\"}"
        when:
            sendMessageAndWaitForMessageAppear(informationTopic, key, value, 1)
        then:
            1 * translationServiceMock.translate(key, {
                assert it.getName() == name
                assert it.getDescription() == description
            })

    }
}
