package ru.i_novus.integration.control.ui.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IntegrationAuthController {

    @Value("${security.admin.login.title}")
    private String title;
    @Value("${n2o.auth.registration.enabled}")
    private Boolean registrationEnabled;

    @RequestMapping("/auth")
    public String login(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("registrationEnabled", registrationEnabled);
        return "auth";
    }

    @RequestMapping("/integrationRegistration")
    public String registration() {
        return "registration";
    }

}