package com.trzewik.kafka.domain.translation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TranslationServiceImpl implements TranslationService {
    private final InformationPublisher publisher;
    private final TranslationProvider translator;

    @Override
    public void translate(String key, Information information) {
        Information translated = translator.getTranslated(information);
        publisher.publish(key, translated);
    }
}
