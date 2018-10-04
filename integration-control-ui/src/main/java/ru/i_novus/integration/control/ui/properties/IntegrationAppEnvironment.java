package ru.i_novus.integration.control.ui.properties;

import net.n2oapp.properties.web.WebAppEnvironment;
import net.n2oapp.properties.web.WebApplicationProperties;


public class IntegrationAppEnvironment extends WebAppEnvironment {

    private WebApplicationProperties properties;


    public IntegrationAppEnvironment(WebApplicationProperties erzProperties) {
        this.properties = erzProperties;

    }

    public WebApplicationProperties getErzProperties() {
        return properties;
    }
}
