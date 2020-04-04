package com.trzewik.kafka.interfaces.kafka.translation

import com.trzewik.kafka.TestKafkaConfig
import com.trzewik.kafka.domain.translation.TranslationService
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@Import(TestKafkaConfig)
@Profile(TestInformationSnapshotConsumerConfig.PROFILE)
@TestConfiguration
class TestInformationSnapshotConsumerConfig {
    final static String PROFILE = 'information-snapshot-consumer-test'

    @Bean
    TranslationService translationServiceMock() {
        return new InformationSnapshotConsumerIT.TranslationServiceMock()
    }
}
