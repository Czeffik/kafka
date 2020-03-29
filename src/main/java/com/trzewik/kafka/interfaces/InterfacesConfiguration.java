package com.trzewik.kafka.interfaces;

import com.trzewik.kafka.interfaces.kafka.translation.InformationConsumerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    InformationConsumerConfiguration.class
})
@Configuration
public class InterfacesConfiguration {
}
