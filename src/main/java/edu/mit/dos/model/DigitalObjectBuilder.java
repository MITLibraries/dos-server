package edu.mit.dos.model;

import java.util.Date;

public class DigitalObjectBuilder {
    private String handle;
    private String title;
    private Date dateCreated;
    private Date dateUpdated;
    private String metadataSource;
    private String contentSource;

    public DigitalObjectBuilder (Date dateCreated, Date dateUpdated, String metadataSource, String contentSource) {
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.metadataSource = metadataSource;
        this.contentSource = contentSource;
    }

        public DigitalObjectBuilder setHandle(String handle){
            this.handle = handle;
            return this;
        }

        public DigitalObjectBuilder setTitle (String title){
            this.title = title;
            return this;
        }


    public DigitalObject createDigitalObject() {
        return new DigitalObject(this);
    }

}
