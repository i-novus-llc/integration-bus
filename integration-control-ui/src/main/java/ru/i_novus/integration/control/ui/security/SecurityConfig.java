package ru.i_novus.integration.control.ui.security;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * Конфигурация spring security
 */
@Configuration
@ComponentScan("net.n2oapp.security.auth.simple")
public class SecurityConfig extends IntegrationSecurityConfigurerAdapter {

    public SecurityConfig(DaoAuthenticationProvider daoAuthenticationProvider) {
        super(daoAuthenticationProvider);
    }

    @Override
    protected void authorize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                                     .ExpressionInterceptUrlRegistry url) {
        url.anyRequest().authenticated();
    }
}