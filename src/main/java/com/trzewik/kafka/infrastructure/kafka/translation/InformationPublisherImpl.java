package com.trzewik.kafka.infrastructure.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import com.trzewik.kafka.domain.translation.InformationPublisher;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class InformationPublisherImpl implements InformationPublisher {
    private final KafkaOperations<String, Information> operations;
    private final String topicName;

    @Override
    public void publish(String key, Information information) {
        ListenableFuture<SendResult<String, Information>> future = operations.send(topicName, key, information);
        future.addCallback(new InformationCallback());
    }
}
