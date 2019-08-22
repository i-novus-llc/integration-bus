package ru.i_novus.integration.configuration;

import net.n2oapp.platform.jaxrs.autoconfigure.EnableJaxRsProxyClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.i_novus.ms.audit.client.AuditClient;
import ru.i_novus.ms.audit.client.impl.SimpleAuditClientImpl;
import ru.i_novus.ms.audit.client.impl.converter.RequestConverter;
import ru.i_novus.ms.audit.client.model.User;
import ru.i_novus.ms.audit.service.api.AuditRest;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootConfiguration
@EnableSwagger2
public class BackendConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant("/**")).build();
    }

    @Bean
    public RequestConverter requestConverter(){
        return new RequestConverter(
                () -> new User("UNKNOWN", "UNKNOWN"),
                () -> "SOURCE_APPLICATION",
                () -> "SOURCE_WORKSTATION"
        );
    }

    @Configuration
    @EnableJaxRsProxyClient(
            classes = {AuditRest.class},
            address = "${audit.rest.url}")
    static class AuditClientConfiguration {
        @Bean
        public AuditClient simpleAuditClient(@Qualifier("auditRestJaxRsProxyClient") AuditRest auditRest) {
            SimpleAuditClientImpl simpleAuditClient = new SimpleAuditClientImpl();
            simpleAuditClient.setAuditRest(auditRest);
            return simpleAuditClient;
        }
    }

}
