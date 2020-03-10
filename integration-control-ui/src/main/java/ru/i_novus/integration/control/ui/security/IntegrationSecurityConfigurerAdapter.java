package ru.i_novus.integration.control.ui.security;

import net.n2oapp.security.auth.N2oSecurityConfigurerAdapter;
import net.n2oapp.security.auth.N2oUrlAuthenticationEntryPoint;
import net.n2oapp.security.auth.simple.SimpleAuthConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;

/**
 * Адаптер для настройки безопасности с простой аутентификацией по логину и паролю
 */
@Import({SimpleAuthConfig.class, IntegrationAuthController.class})
public abstract class IntegrationSecurityConfigurerAdapter extends N2oSecurityConfigurerAdapter {
    @Value("${n2o.api.url:/n2o}")
    private String n2oUrl;
    private DaoAuthenticationProvider daoAuthenticationProvider;

    public IntegrationSecurityConfigurerAdapter(DaoAuthenticationProvider daoAuthenticationProvider) {
        this.daoAuthenticationProvider = daoAuthenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider);
    }

    @Override
    protected ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry beforeAuthorize(HttpSecurity http) throws Exception {
        return configureAuthorizeAuthRequests(super.beforeAuthorize(http));
    }

    public ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry configureAuthorizeAuthRequests(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry url) throws Exception {
        return ((ExpressionUrlAuthorizationConfigurer.AuthorizedUrl) url.antMatchers(new String[]{"/registration/**", "/registrationServlet/**", "/dist/**", "/cad/**", "/favicon.ico"})).permitAll();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        configureExceptionHandling(http.exceptionHandling());
        authorize(beforeAuthorize(http));
        configureCadLogin(http.formLogin());
        configureCadLogout(http.logout());
        http.headers().contentTypeOptions().disable();
        http.csrf().disable();

    }

    protected HttpSecurity configureExceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> exceptionHandling) throws Exception {
        return exceptionHandling.authenticationEntryPoint(new N2oUrlAuthenticationEntryPoint("/auth", this.n2oUrl)).and();
    }

    public HttpSecurity configureCadLogin(FormLoginConfigurer<HttpSecurity> login) {
        return login.loginPage("/auth").permitAll()
                .loginProcessingUrl("/auth")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/auth?error=true")
                .defaultSuccessUrl("/")
                .and();

    }

    public HttpSecurity configureCadLogout(LogoutConfigurer<HttpSecurity> logout) throws Exception {
        return logout.logoutUrl("/logout").permitAll()
                .logoutSuccessUrl("/auth")
                .deleteCookies("JSESSIONID")
                .and().rememberMe().key("uniqueKey").and();
    }
}
