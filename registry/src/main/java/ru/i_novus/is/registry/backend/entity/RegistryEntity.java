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

    @Column(name = "host")
    private String host;


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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
