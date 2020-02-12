package edu.mit.dos.model;

import java.util.Date;

public class DigitalObjectBuilder {
    private String handle;
    private String title;
    private Date dateCreated;
    private Date dateUpdated;
    private String metadataSource;
    private String contentSource;

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getMetadataSource() {
        return metadataSource;
    }

    public void setMetadataSource(String metadataSource) {
        this.metadataSource = metadataSource;
    }

    public String getContentSource() {
        return contentSource;
    }

    public void setContentSource(String contentSource) {
        this.contentSource = contentSource;
    }

    public DigitalObjectBuilder (Date dateCreated, Date dateUpdated, String metadataSource, String contentSource, String handle, String title) {
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.metadataSource = metadataSource;
        this.contentSource = contentSource;
        this.handle = handle;
        this.title = title;
    }

    public DigitalObject createDigitalObject() {
        return new DigitalObject(this);
    }

}
