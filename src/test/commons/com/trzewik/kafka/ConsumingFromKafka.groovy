package com.trzewik.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.OffsetResetStrategy
import org.apache.kafka.common.PartitionInfo
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.serialization.StringDeserializer

import java.time.Duration

import static org.awaitility.Awaitility.await

trait ConsumingFromKafka {
    final static String TEST_GROUP_ID = 'AutomatedTestGroupId'

    abstract Duration getDefaultDuration()

    abstract String getBrokers()

    List<KafkaMessage> consumeAllFrom(String topicName, int expectedNumberOfMessages) {
        def messages = []
        await().atMost(getDefaultDuration()).untilAsserted {
            messages = consumeAllFrom(topicName)

            assert messages.size() == expectedNumberOfMessages
        }
        return messages
    }

    List<KafkaMessage> consumeAllFrom(String topicName) {
        def consumer = createConsumer()
        def topicPartitions = getTopicPartitions(consumer, topicName)

        consumer.assign(topicPartitions)
        consumer.seekToBeginning(topicPartitions)

        List<KafkaMessage> messages = []
        Map<TopicPartition, Long> endOffsets = consumer.endOffsets(topicPartitions)
        topicPartitions.each { TopicPartition p ->
            while (endOffsets.get(p) > consumer.position(p)) {
                consumer.poll(Duration.ofMillis(100)).each {
                    messages << new KafkaMessage(it)
                }
            }
        }
        consumer.close()    //todo should be in finally block

        return messages
    }


    private List<TopicPartition> getTopicPartitions(KafkaConsumer<String, String> consumer, String topicName) {
        return getPartitionInfos(consumer, topicName).collect { PartitionInfo pi -> return new TopicPartition(pi.topic(), pi.partition()) }
    }

    private List<PartitionInfo> getPartitionInfos(KafkaConsumer<String, String> consumer, String topicName) {
        return consumer.partitionsFor(topicName)
    }

    private KafkaConsumer<String, String> createConsumer() {
        return new KafkaConsumer<String, String>(kafkaConsumerProperties())
    }

    private Properties kafkaConsumerProperties() {
        Properties props = new Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, getBrokers())
        props.put(ConsumerConfig.GROUP_ID_CONFIG, TEST_GROUP_ID)
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, 'false')
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, OffsetResetStrategy.EARLIEST.name().toLowerCase())
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class)
        return props
    }

    static class KafkaMessage {
        String key
        String value

        KafkaMessage(ConsumerRecord<String, String> consumerRecord) {
            key = consumerRecord.key()
            value = consumerRecord.value()
        }
    }
}
