package com.trzewik.kafka.domain.translation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TranslationServiceFactory {
    public static TranslationService create(InformationPublisher informationPublisher, TranslationProvider translationProvider) {
        return new TranslationServiceImpl(informationPublisher, translationProvider);
    }
}
