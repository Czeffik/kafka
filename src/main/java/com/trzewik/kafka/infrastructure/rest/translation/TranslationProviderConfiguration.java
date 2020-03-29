package com.trzewik.kafka.infrastructure.rest.translation;

import com.trzewik.kafka.domain.translation.TranslationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslationProviderConfiguration {
    @Bean
    TranslationProvider translationProvider() {
        return new TranslationProviderImpl();
    }
}
