package com.trzewik.kafka.interfaces.kafka.translation

import com.trzewik.kafka.domain.translation.TranslationService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import spock.mock.DetachedMockFactory

@Profile(TestInformationConsumerConfig.PROFILE)
@TestConfiguration
class TestInformationConsumerConfig {
    final static String PROFILE = 'information-consumer-test'

    DetachedMockFactory factory = new DetachedMockFactory()

    @Bean
    TranslationService translationServiceMock() {
        return factory.Mock(TranslationService)
    }
}
