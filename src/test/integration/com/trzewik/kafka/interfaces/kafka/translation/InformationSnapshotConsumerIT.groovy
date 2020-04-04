package com.trzewik.kafka.interfaces.kafka.translation

import com.trzewik.kafka.EmbeddedKafkaTest
import com.trzewik.kafka.KafkaTestHelper
import com.trzewik.kafka.KafkaTestHelperFactory
import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.TranslationService
import groovy.util.logging.Slf4j
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.awaitility.Awaitility
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.Environment
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.ContextCustomizer
import org.springframework.test.context.MergedContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.support.AnnotationConfigContextLoader
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@ActiveProfiles(['test', TestInformationSnapshotConsumerConfig.PROFILE])
@ContextConfiguration(classes = [
    TestInformationSnapshotConsumerConfig,
    InformationConsumerConfiguration
]
    , loader = Loader
)
@TestPropertySource(
    properties = [
        'kafka.topic.information=TopicInformationSnapshotConsumerIT',
        'kafka.group.id=GroupIdInformationSnapshotConsumerIT'
    ]
)
@EmbeddedKafkaTest
class InformationSnapshotConsumerIT extends Specification {
    @Autowired
    TranslationService translationServiceMock

    @Autowired
    KafkaTestHelper<String> informationTopicHelper

    def 'should consume all messages from topic when consumer started and consume next messages'() {
        given:
            def startingMessages = Loader.startingMessages()
        expect:
            Awaitility.await().atMost(Duration.ofSeconds(4)).untilAsserted {
                with((translationServiceMock as TranslationServiceMock).getMessages()) {
                    it.size() == startingMessages.size()
                    it.keySet().containsAll(startingMessages.keySet())
                }
            }
        when:
            def key = 'next key'
            def value = '{"name": "next message name", "description": "next message description"}'
            informationTopicHelper.sendMessageAndWaitForAppear(key, value)
        then:
            Awaitility.await().atMost(Duration.ofSeconds(4)).untilAsserted {
                with((translationServiceMock as TranslationServiceMock).getMessages()) {
                    it.size() == startingMessages.size() + 1
                    it.keySet().contains(key)
                }
            }
    }

    @Slf4j
    static class TranslationServiceMock implements TranslationService {
        final Map<String, Information> messages = new ConcurrentHashMap<>()

        @Override
        void translate(String key, Information information) {
            log.info('Mock translator received message with key: [{}], and information: [{}]', key, information)
            messages.put(key, information)
            log.info('Mock translator has [{}] messages with keys: [{}]', messages.size(), messages.keySet())
        }

        synchronized Map<String, Information> getMessages() {
            return messages
        }
    }

    static class Loader extends AnnotationConfigContextLoader {
        @Override
        protected void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
            for (ContextCustomizer contextCustomizer : mergedConfig.getContextCustomizers()) {
                contextCustomizer.customizeContext(context, mergedConfig)
            }
            sendMessages(context)
        }

        static void sendMessages(ConfigurableApplicationContext context) {
            KafkaTestHelper<String> informationTopicHelper = createHelper(context.getEnvironment())
            informationTopicHelper.sendMessagesAndWaitForAppear(startingMessages())
            informationTopicHelper.close()
        }

        static KafkaTestHelper<String> createHelper(Environment environment) {
            return KafkaTestHelperFactory.create(
                new KafkaTestHelperFactory.Builder(
                    topic: environment.getProperty('kafka.topic.information'),
                    brokers: environment.getProperty('kafka.bootstrap.address'),
                    serializer: new StringSerializer(),
                    deserializer: new StringDeserializer()
                )
            )
        }

        static Map<String, String> startingMessages() {
            Map<String, String> messages = [:]
            14.times {
                String description = 'starting message description' + it
                String value = "{\"name\":\"name\",\"description\":\"${description}\"}"
                messages.put('starting message key' + it, value)
            }
            return messages
        }
    }
}
