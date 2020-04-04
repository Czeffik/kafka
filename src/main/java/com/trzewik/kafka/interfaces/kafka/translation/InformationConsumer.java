package com.trzewik.kafka.interfaces.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import com.trzewik.kafka.domain.translation.TranslationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.ConsumerSeekAware;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class InformationConsumer implements ConsumerSeekAware {
    private final TranslationService translationService;
    private final String topicName;

    @KafkaListener(topics = "${kafka.topic.information}", containerFactory = "informationKafkaListenerFactory")
    public void read(ConsumerRecord<String, Information> record) {
        log.info("Consumed information! Key: [{}], information: [{}]!", record.key(), record.value());
        translationService.translate(record.key(), record.value());
    }

    @Override
    public void onPartitionsAssigned(Map<TopicPartition, Long> assignments, ConsumerSeekCallback callback) {
        assignments.keySet().stream()
            .filter(p -> p.topic().equals(topicName))
            .forEach(p -> callback.seekToBeginning(topicName, p.partition()));
    }
}
