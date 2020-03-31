package edu.mit.dos.model;

import javax.persistence.*;

/**
 * Representation of a file object
 */
@Entity
@Table(name = "file")
public class DigitalFile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long fid;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "size")
    private Long size;

    @Column(name = "checksum")
    private String checksum;

    @Column(name = "path")
    private String path;

    @Column(name ="oid")
    private long oid;

    @Column(name = "handle")
    private String handle;

    public DigitalFile() {

    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    @Override
    public String toString() {
        return "DigitalFile{" +
                "fid=" + fid +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", size=" + size +
                ", checksum='" + checksum + '\'' +
                ", path='" + path + '\'' +
                ", oid=" + oid +
                ", handle='" + handle + '\'' +
                '}';
    }
}
