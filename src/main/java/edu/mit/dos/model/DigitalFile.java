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
    private long oid;

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


    public DigitalFile() {

    }

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
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

    @Override
    public String toString() {
        return "DigitalFile{" +
                "oid=" + oid +
                ", name='" + name +
                ", type=" + type +
                ", size=" + size +
                ", checksum='" + checksum +
                ", path='" + path +
                '}';
    }
}
