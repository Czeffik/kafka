package com.trzewik.kafka.infrastructure.rest.translation;

import com.trzewik.kafka.domain.translation.Information;
import com.trzewik.kafka.domain.translation.TranslationProvider;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class TranslationProviderImpl implements TranslationProvider {
    //TODO should use DTO
    @Override
    public Information getTranslated(Information information) {
        return new Information(information.getName(), "Translated description");
    }
}
