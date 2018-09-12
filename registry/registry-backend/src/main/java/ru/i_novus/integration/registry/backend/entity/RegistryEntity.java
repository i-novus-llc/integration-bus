package ru.i_novus.integration.registry.backend.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "integration", name = "registry")
public class RegistryEntity {

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "url")
    private String url;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
