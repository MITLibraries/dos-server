package edu.mit.dos.model;

import org.json.simple.JSONObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

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
    private Date dateUpdated;

    @Column(name = "metadata_source")
    private String metadataSource;

    @Column(name = "content_source")
    private String contentSource;

    @OneToMany
    private List<DigitalFile> files =new ArrayList<>();

    public DigitalObject() {

    }

    public DigitalObject(String title, String handle, Date dateCreated, Date dateUpdated, String metadataSource, String contentSource) {
        this.title = title;
        this.handle = handle;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.metadataSource = metadataSource;
        this.contentSource = contentSource;
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

    public void setTitle(String title) {
        this.title = title;
    }

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

    public List<DigitalFile> getFiles() {
        return files;
    }

    public void setFiles(List<DigitalFile> files) {
        this.files = files;
    }

    @Override
    public String toString() {
        return "DigitalObject{" +
                "oid=" + oid +
                ", title='" + title + '\'' +
                ", handle='" + handle + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", metadataSource='" + metadataSource + '\'' +
                ", contentSource='" + contentSource + '\'' +
                ", files=" + files +
                '}';
    }

    public String postResponse() {
        JSONObject response = new JSONObject();
        response.put("oid", oid);
        List<Long> fids = new ArrayList<>();
        for (DigitalFile file : files) {
            long fid = file.getFid();
            fids.add(fid);
        }
        response.put("files", fids);
        return response.toJSONString();
    }
}
