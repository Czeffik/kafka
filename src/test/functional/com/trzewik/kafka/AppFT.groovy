package com.trzewik.kafka

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles(['test'])
@SpringBootTest(
    classes = [App],
    properties = [
        'topic.translated=AppTranslated',
        'topic.information=AppInformation'
    ]
)
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
            String key = 'example key'
            String value = '{"name":"name","description":"description"}'
        when:
            sendMessage(informationTopic, key, value)
        then:
            def messages = consumeAllFrom(translatedTopic, 1)
            with(messages.first()) {
                assert it.key == key
                assert it.value == '{"name":"name","description":"Translated description"}'
            }
    }
}