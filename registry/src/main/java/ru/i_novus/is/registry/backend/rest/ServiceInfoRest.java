package ru.i_novus.is.registry.backend.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.i_novus.is.registry.backend.entity.RegistryEntity;
import ru.i_novus.is.registry.backend.repository.RegistryRepository;

import java.util.Optional;

@RestController
@RequestMapping("/registry/service")
public class ServiceInfoRest {

    @Autowired
    RegistryRepository registryRepository;

    @GetMapping("/info/{code}")
    public String getServiceInfo(@PathVariable String code) {
        Optional<RegistryEntity> registryEntity =  registryRepository.findById(code);

        return registryEntity.get().getUrl();
    }
}
