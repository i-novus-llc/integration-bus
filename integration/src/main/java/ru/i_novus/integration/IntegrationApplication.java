package ru.i_novus.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication(scanBasePackages = "ru.i_novus.integration")
@ImportResource({"classpath*:/spring-integration-config.xml", "classpath*:/si-sync-config.xml"})
public class IntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntegrationApplication.class, args);
    }
}
