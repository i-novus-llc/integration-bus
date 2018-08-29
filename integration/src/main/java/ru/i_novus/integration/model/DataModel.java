package ru.i_novus.integration.model;

import java.io.Serializable;

public class DataModel implements Serializable {
    private String path;
    private String name;
    private String mime;
    private DataModel attachment;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMime() {
        return mime;
    }

    public void setMime(String mime) {
        this.mime = mime;
    }

    public DataModel getAttachment() {
        return attachment;
    }

    public void setAttachment(DataModel attachment) {
        this.attachment = attachment;
    }
}
