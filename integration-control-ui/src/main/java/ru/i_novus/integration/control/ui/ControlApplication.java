package ru.i_novus.integration.control.ui;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import ru.i_novus.integration.control.ui.security.SecurityConfig;

@SpringBootApplication
//@Import(SecurityConfig.class)
public class ControlApplication {

    public static void main(String[] args) {
        SpringApplication.run(ControlApplication.class, args);
    }


}
