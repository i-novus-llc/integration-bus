package ru.i_novus.integration.control.ui;

import net.n2oapp.security.admin.rest.client.AdminRestClientConfiguration;
import net.n2oapp.security.admin.web.AdminWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.i_novus.integration.web.IntegrationWebConfiguration;

@SpringBootApplication
@Import({IntegrationWebConfiguration.class, AdminWebConfiguration.class, AdminRestClientConfiguration.class})
public class ControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlApplication.class, args);
    }


}
