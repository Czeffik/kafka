package com.trzewik.kafka.domain.translation;

import java.util.Map;

public interface InformationPublisher {

    default void publishAll(Map<String, Information> information) {
        information.forEach(this::publish);
    }

    void publish(String key, Information information);
}
