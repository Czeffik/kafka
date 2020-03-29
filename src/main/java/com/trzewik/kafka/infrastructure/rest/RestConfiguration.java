package com.trzewik.kafka.infrastructure.rest;

import com.trzewik.kafka.infrastructure.rest.translation.TranslationProviderConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({
    TranslationProviderConfiguration.class
})
@Configuration
public class RestConfiguration {
}
