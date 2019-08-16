package ru.i_novus.integration.control.ui.security;

import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация spring security
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfig {// extends N2oSecurityConfigurerAdapter {

//    public SecurityConfig(DaoAuthenticationProvider daoAuthenticationProvider) {
//        super(daoAuthenticationProvider);
//    }

/*    @Override
    protected void authorize(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry url) throws Exception {
        url.anyRequest().permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        super.configure(web);
        web.ignoring().antMatchers("/*");
    }*/
}
