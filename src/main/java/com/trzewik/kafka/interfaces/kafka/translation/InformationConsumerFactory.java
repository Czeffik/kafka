package com.trzewik.kafka.interfaces.kafka.translation;

import com.trzewik.kafka.domain.translation.TranslationService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InformationConsumerFactory {
    public static InformationConsumer create(TranslationService translationService) {
        return new InformationConsumerImpl(translationService);
    }
}
