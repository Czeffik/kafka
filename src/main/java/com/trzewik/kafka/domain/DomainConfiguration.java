package com.trzewik.kafka.domain;

import com.trzewik.kafka.domain.translation.InformationPublisher;
import com.trzewik.kafka.domain.translation.TranslationProvider;
import com.trzewik.kafka.domain.translation.TranslationService;
import com.trzewik.kafka.domain.translation.TranslationServiceFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfiguration {
    @Bean
    TranslationService translationService(InformationPublisher informationPublisher, TranslationProvider translationProvider) {
        return TranslationServiceFactory.create(informationPublisher, translationProvider);
    }
}
