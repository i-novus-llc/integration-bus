package ru.i_novus.integration.registry.backend;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.n2oapp.platform.loader.server.ServerLoader;
import net.n2oapp.platform.loader.server.ServerLoaderRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.i_novus.integration.registry.backend.loader.JsonLocalLoaderRunner;

import java.util.List;

@SpringBootApplication
@ComponentScan({"ru.i_novus.integration.registry.backend"})
@EnableJpaRepositories(basePackages = "ru.i_novus.integration.registry.backend")
@EntityScan("ru.i_novus.integration.registry.backend")
public class RegistryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegistryBackendApplication.class, args);
    }

    @Bean
    public ServerLoaderRunner localLoader(List<ServerLoader> loaders) {
        return new JsonLocalLoaderRunner(loaders, new ObjectMapper());
    }
}
