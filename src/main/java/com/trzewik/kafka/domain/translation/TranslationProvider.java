package com.trzewik.kafka.domain.translation;

public interface TranslationProvider {

    Information getTranslated(Information information);
}
