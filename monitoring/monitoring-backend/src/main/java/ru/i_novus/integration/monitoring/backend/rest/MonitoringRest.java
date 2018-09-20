package ru.i_novus.integration.monitoring.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.i_novus.integration.monitoring.backend.entity.MonitoringEntity;
import ru.i_novus.integration.monitoring.backend.repository.MonitoringRepository;
import ru.i_novus.is.integration.common.api.MonitoringModel;

@RestController
@RequestMapping("/monitoring/service")
public class MonitoringRest {

    @Autowired
    MonitoringRepository repository;

    @PostMapping(value = "/save")
    public void getServiceInfo(@RequestBody MonitoringModel model) {
        repository.save(new MonitoringEntity(model));
    }
}
