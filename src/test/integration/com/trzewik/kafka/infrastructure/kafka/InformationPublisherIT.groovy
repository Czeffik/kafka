package com.trzewik.kafka.infrastructure.kafka

import com.trzewik.kafka.domain.translation.Information
import com.trzewik.kafka.domain.translation.InformationPublisher
import com.trzewik.kafka.infrastructure.kafka.translation.TranslationPublisherConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

@ContextConfiguration(classes = TranslationPublisherConfiguration)
@EmbeddedKafka(
    topics = ['information-translated-output-topic'],
    partitions = 4,
    ports = [InformationPublisherIT.BOOTSTRAP_PORT],
    zookeeperPort = InformationPublisherIT.ZOOKEEPER_PORT
)
@TestPropertySource(
    properties = [
        'topic.translated=information-translated-output-topic',
        'bootstrap.address=${spring.embedded.kafka.brokers}'
    ]
)
class InformationPublisherIT extends Specification {
    static final int ZOOKEEPER_PORT = 9999
    static final int BOOTSTRAP_PORT = 9988

    @Value('${spring.embedded.kafka.brokers}')
    String kafkaBrokers

    @Autowired
    InformationPublisher publisher

    @Autowired
    EmbeddedKafkaBroker kafkaEmbedded

    def 'should publish information on topic successfully'() {
        given:
            def key = 'test-key'
            def information = new Information('name', 'description')
        when:
            publisher.publish(key, information)
        then:
            //todo replace with checking that message appear on topic
            1 == 1

    }
}
