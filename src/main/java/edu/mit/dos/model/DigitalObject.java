package edu.mit.dos.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Representation of a digital object
 */

@Entity
@Table(name = "digital_object")
public class DigitalObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long oid;

    @Column(name = "title")
    private String title;

    @Column(name = "handle")
    private String handle;

    @Column(name = "creation_date")
    private Date dateCreated;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name="metadata_system")
    private String metadataSource;

    @Column(name="source_system")
    private String sourceSystem;

    public DigitalObject() {

    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getMetadataSource() {
        return metadataSource;
    }

    public void setMetadataSource(String metadataSource) {
        this.metadataSource = metadataSource;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }


    @Override
    public String toString() {
        return "DigitalObject{" +
                "oid=" + oid +
                ", title='" + title + '\'' +
                ", handle='" + handle + '\'' +
                ", dateCreated=" + dateCreated +
                ", updateDate=" + updateDate +
                ", metadataSource='" + metadataSource + '\'' +
                ", sourceSystem='" + sourceSystem + '\'' +
                '}';
    }
}
