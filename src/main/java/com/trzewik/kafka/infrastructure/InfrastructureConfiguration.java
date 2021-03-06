package com.trzewik.kafka.infrastructure;

import com.trzewik.kafka.infrastructure.kafka.translation.TranslationPublisherConfiguration;
import com.trzewik.kafka.infrastructure.rest.RestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    RestConfiguration.class,
    TranslationPublisherConfiguration.class
})
@Configuration
public class InfrastructureConfiguration {
}
