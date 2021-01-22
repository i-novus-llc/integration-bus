package ru.i_novus.integration.registry.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages="ru.i_novus.integration.registry")
public class RegistryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryBackendApplication.class, args);
    }
}
