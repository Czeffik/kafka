package com.trzewik.kafka.interfaces;

import com.trzewik.kafka.interfaces.kafka.KafkaConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    KafkaConfiguration.class
})
@Configuration
public class InterfacesConfiguration {
}
