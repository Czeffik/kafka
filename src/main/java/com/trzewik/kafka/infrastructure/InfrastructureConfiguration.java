package com.trzewik.kafka.infrastructure;

import com.trzewik.kafka.infrastructure.kafka.KafkaConfiguration;
import com.trzewik.kafka.infrastructure.rest.RestConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    RestConfiguration.class,
    KafkaConfiguration.class
})
@Configuration
public class InfrastructureConfiguration {
}
