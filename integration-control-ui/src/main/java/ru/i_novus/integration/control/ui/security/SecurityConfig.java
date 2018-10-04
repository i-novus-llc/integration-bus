package ru.i_novus.integration.control.ui.security;

import net.n2oapp.security.admin.sql.AdminSqlConfiguration;
import net.n2oapp.security.auth.simple.SimpleSecurityConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

/**
 * Конфигурация spring security
 */
@Configuration
@EnableWebSecurity
@Import(AdminSqlConfiguration.class)
public class SecurityConfig extends SimpleSecurityConfigurerAdapter {

    public SecurityConfig(DaoAuthenticationProvider daoAuthenticationProvider) {
        super(daoAuthenticationProvider);
    }

    @Override
    protected void authorize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry url) throws Exception {
        url.anyRequest().authenticated();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/mtr/login/**");
    }
}
