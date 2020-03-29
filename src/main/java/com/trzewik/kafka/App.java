package com.trzewik.kafka;

import com.trzewik.kafka.infrastructure.DomainConfiguration;
import com.trzewik.kafka.infrastructure.InfrastructureConfiguration;
import com.trzewik.kafka.interfaces.InterfacesConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.annotation.Import;

@Import({
    InterfacesConfiguration.class,
    InfrastructureConfiguration.class,
    DomainConfiguration.class
})
@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
