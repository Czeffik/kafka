package com.trzewik.kafka.interfaces;

import com.trzewik.kafka.interfaces.kafka.KafkaConsumerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    KafkaConsumerConfiguration.class
})
@Configuration
public class InterfacesConfiguration {
}
