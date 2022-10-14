package ru.i_novus.integration.control.ui;

import net.n2oapp.framework.boot.N2oSqlAutoConfiguration;
import net.n2oapp.security.admin.api.service.UserDetailsService;
import net.n2oapp.security.admin.rest.client.AdminRestClientConfiguration;
import net.n2oapp.security.admin.web.AdminWebConfiguration;
import net.n2oapp.security.auth.common.AuthoritiesPrincipalExtractor;
import net.n2oapp.security.auth.common.UserAttributeKeys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

@SpringBootApplication(exclude = {N2oSqlAutoConfiguration.class})
@Import({AdminWebConfiguration.class, AdminRestClientConfiguration.class})
public class ControlApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ControlApplication.class, args);
    }

    @Bean
    @Primary
    public AuthoritiesPrincipalExtractor principalExtractor(UserDetailsService service, UserAttributeKeys userAttributeKeys) {
        return new AuthoritiesPrincipalExtractor(service, "system", userAttributeKeys);
    }

}
