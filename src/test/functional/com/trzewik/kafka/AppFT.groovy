package com.trzewik.kafka

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles(['test'])
@SpringBootTest(
    classes = [App, TestKafkaConfig],
    properties = [
        'kafka.topic.translated=TopicAppTranslated',
        'kafka.topic.information=TopicAppInformation',
        'kafka.group.id=GroupIdAppFT'
    ],
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@EmbeddedKafkaTest
class AppFT extends Specification {
    @Autowired
    KafkaTestHelper<String> informationTopicHelper
    @Autowired
    KafkaTestHelper<String> translatedTopicHelper

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
