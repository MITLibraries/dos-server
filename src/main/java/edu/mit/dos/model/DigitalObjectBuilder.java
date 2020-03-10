package edu.mit.dos.model;

import java.util.Date;

public class DigitalObjectBuilder {

    private String title;
    private String handle;
    private Date dateCreated;
    private Date dateUpdated;
    private String metadataSource;
    private String contentSource;

    public DigitalObjectBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public DigitalObjectBuilder setHandle(String handle) {
        this.handle = handle;
        return this;
    }

    public DigitalObjectBuilder setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public DigitalObjectBuilder setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
        return this;
    }
    public DigitalObjectBuilder setMetadataSource(String metadataSource) {
        this.metadataSource = metadataSource;
        return this;
    }

    public DigitalObjectBuilder setContentSource(String contentSource) {
        this.contentSource = contentSource;
        return this;
    }

    public DigitalObject createDigitalObject() {
        return new DigitalObject(title, handle, dateCreated, dateUpdated, metadataSource, contentSource);
    }
}