package com.trzewik.kafka.interfaces.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import com.trzewik.kafka.domain.translation.TranslationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class InformationConsumerImpl implements InformationConsumer {
    private final TranslationService translationService;

    @Override
    @KafkaListener(topics = "${topic.information}", containerFactory = "informationKafkaListenerFactory")
    public void read(ConsumerRecord<String, Information> record) {
        log.info("Consumed information! Key: [{}], information: [{}]!", record.key(), record.value());
        translationService.translate(record.key(), record.value());
    }
}
