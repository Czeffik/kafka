package com.trzewik.kafka

import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(['test'])
@SpringBootTest(
    classes = [App],
    properties = [
        'topic.translated=AppTranslated',
        'topic.information=AppInformation'
    ]
)
@DirtiesContext
@Slf4j
class AppFT extends KafkaSpecification {
    @Value('${topic.information}')
    String informationTopic

    @Value('${topic.translated}')
    String translatedTopic

    def '''should consume message from information topic
            and translate consumed information
            and publish on translated topic
        '''() {
        given:
            String key = 'new key for this topic'
            String value = '{"name":"other name than","description":"super description"}'
        when:
            sendMessage(informationTopic, key, value)
        then:
            def messages = consumeAllFrom(translatedTopic, 1)
            with(messages.first()) {
                assert it.key == key
                assert it.value == '{"name":"other name than","description":"Translated description"}'
            }
    }
}
