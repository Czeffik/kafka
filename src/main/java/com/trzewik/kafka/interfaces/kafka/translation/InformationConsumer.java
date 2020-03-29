package com.trzewik.kafka.interfaces.kafka.translation;

import com.trzewik.kafka.domain.translation.Information;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface InformationConsumer {

    void read(ConsumerRecord<String, Information> record);
}
