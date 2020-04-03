package com.trzewik.kafka.consumer

import groovy.transform.PackageScope
import groovy.util.logging.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.PartitionInfo
import org.apache.kafka.common.TopicPartition

import java.time.Duration

@Slf4j
class KafkaTestConsumer<V> implements AutoCloseable {
    private final KafkaConsumer<String, V> consumer
    final String topic
    final List<TopicPartition> partitions
    final Duration defaultPollDuration

    @PackageScope
    KafkaTestConsumer(KafkaConsumer<String, V> consumer, String topic, long defaultPollTimeout = 500) {
        this.consumer = consumer
        this.topic = topic
        this.defaultPollDuration = Duration.ofMillis(defaultPollTimeout)
        this.partitions = getTopicPartitions()

        consumer.assign(partitions)
        consumer.seekToBeginning(partitions)
    }

    /**
     * Method consume all messages from current offset to end offset.
     * End offset is read once - after this method is called
     * @return list with consumed messages
     */
    List<KafkaMessage<V>> consumeAll() {
        List<KafkaMessage<V>> messages = []
        Map<TopicPartition, Long> endOffsets = getEndOffsets()
        topicPartitions.each { TopicPartition p ->
            long endOffset = endOffsets.get(p)
            log.info('Started polling messages from topic: [{}], partition: [{}] to offset: [{}]', topic, p.partition(), endOffset)

            List<KafkaMessage<V>> partitionMessages = []
            while (getPosition(p) < endOffset) {
                poll().each { partitionMessages << new KafkaMessage(it) }
            }

            messages.addAll(partitionMessages)
            log.info('Consumed [{}] messages from topic: [{}], partition: [{}]', partitionMessages.size(), topic, p.partition())
        }
        return messages
    }

    ConsumerRecords<String, V> poll() {
        return consumer.poll(defaultPollDuration)
    }

    Map<TopicPartition, Long> getPositions() {
        return topicPartitions.collectEntries {
            [(it): getPosition(it)]
        }
    }

    long getPosition(TopicPartition partition) {
        return consumer.position(partition)
    }

    Map<TopicPartition, Long> getEndOffsets() {
        return consumer.endOffsets(topicPartitions)
    }

    Map<TopicPartition, Long> getBeginningOffsets() {
        return consumer.beginningOffsets(topicPartitions)
    }

    void seekToBeginning() {
        consumer.seekToBeginning(partitions)
        //poll is required because seek to beginning is lazy
        consumer.poll(Duration.ofMillis(1))
    }

    void seekToEnd() {
        consumer.seekToEnd(partitions)
        //poll is required because seek to end is lazy
        consumer.poll(Duration.ofMillis(1))
    }

    private List<TopicPartition> getTopicPartitions() {
        return getPartitionInfos().collect { PartitionInfo pi ->
            return new TopicPartition(pi.topic(), pi.partition())
        }
    }

    private List<PartitionInfo> getPartitionInfos() {
        return consumer.partitionsFor(topic)
    }

    @Override
    void close() throws Exception {
        consumer.close()
    }
}
