package com.trzewik.kafka.interfaces.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import com.trzewik.kafka.domain.translation.TranslationService;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG;
import static org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG;

@EnableKafka
@Configuration
public class InformationConsumerConfiguration {
    @Bean
    InformationConsumer informationConsumer(
        TranslationService translationService,
        @Value("${kafka.topic.information}") String topicName
    ) {
        return new InformationConsumer(translationService, topicName);
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, Information> informationKafkaListenerFactory(
        ConsumerFactory<String, Information> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, Information> cklcf = new ConcurrentKafkaListenerContainerFactory<>();

        cklcf.setConcurrency(1);
        cklcf.setConsumerFactory(consumerFactory);

        return cklcf;
    }

    @Bean
    ConsumerFactory<String, Information> consumerFactory(
        @Value("${kafka.bootstrap.address}") String bootstrapAddress,
        @Value("${kafka.group.id}") String groupId
    ) {
        Map<String, Object> config = new HashMap<>();
        config.put(BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        config.put(GROUP_ID_CONFIG, groupId);
        config.put(AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
            config,
            new StringDeserializer(),
            new JsonDeserializer<>(Information.class)
        );
    }
}
