package com.trzewik.kafka;


import com.trzewik.kafka.domain.DomainConfiguration;
import com.trzewik.kafka.infrastructure.InfrastructureConfiguration;
import com.trzewik.kafka.interfaces.InterfacesConfiguration;
import org.springframework.context.annotation.Import;

@Import({
    InterfacesConfiguration.class,
    InfrastructureConfiguration.class,
    DomainConfiguration.class
})
public class App {
    public static void main(String[] args) {

    }
}
