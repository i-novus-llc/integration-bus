package ru.i_novus.integration.configuration;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;

@Configuration
public class WebApplicationContextLocator implements ServletContextInitializer {

    private static WebApplicationContext webApplicationContext;

    public static WebApplicationContext getCurrentWebApplicationContext() {
        return webApplicationContext;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }
}