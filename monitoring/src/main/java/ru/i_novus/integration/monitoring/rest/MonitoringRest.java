package ru.i_novus.integration.monitoring.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.i_novus.integration.monitoring.model.MonitoringModel;
import ru.i_novus.integration.monitoring.repository.MonitoringRepository;

@RestController
@RequestMapping("/monitoring/service")
public class MonitoringRest {

    @Autowired
    MonitoringRepository repository;

    @PostMapping("/save")
    public void getServiceInfo(@RequestBody MonitoringModel model) {

    }
}
