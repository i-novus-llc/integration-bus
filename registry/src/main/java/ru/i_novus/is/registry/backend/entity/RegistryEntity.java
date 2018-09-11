package ru.i_novus.is.registry.backend.entity;

import javax.persistence.*;

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
