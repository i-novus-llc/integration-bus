package ru.i_novus.integration.registry.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@ComponentScan({"ru.i_novus.integration.registry.backend"})
@EnableJpaRepositories(basePackages = "ru.i_novus.integration.registry.backend")
@EntityScan("ru.i_novus.integration.registry.backend")
public class RegistryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryBackendApplication.class, args);
    }

}
