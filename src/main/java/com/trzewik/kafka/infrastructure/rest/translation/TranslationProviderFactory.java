package com.trzewik.kafka.infrastructure.rest.translation;

import com.trzewik.kafka.domain.translation.TranslationProvider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TranslationProviderFactory {
    public static TranslationProvider create() {
        return new TranslationProviderImpl();
    }
}
