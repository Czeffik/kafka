package com.trzewik.kafka.domain.translation;

public interface TranslationService {
    void translate(String key, Information information);
}
