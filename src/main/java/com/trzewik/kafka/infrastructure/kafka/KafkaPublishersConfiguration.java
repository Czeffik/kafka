package com.trzewik.kafka.infrastructure.kafka;

import com.trzewik.kafka.infrastructure.kafka.translation.TranslationPublisherConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    TranslationPublisherConfiguration.class
})
@Configuration
public class KafkaPublishersConfiguration {
}
