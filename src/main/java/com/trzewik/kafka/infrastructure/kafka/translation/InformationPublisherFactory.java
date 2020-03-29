package com.trzewik.kafka.infrastructure.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import com.trzewik.kafka.domain.translation.InformationPublisher;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.kafka.core.KafkaOperations;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InformationPublisherFactory {
    public static InformationPublisher create(KafkaOperations<String, Information> kafkaOperations, String topicName) {
        return new InformationPublisherImpl(kafkaOperations, topicName);
    }
}
