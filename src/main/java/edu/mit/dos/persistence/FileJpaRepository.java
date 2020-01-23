package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FileJpaRepository extends JpaRepository<DigitalFile, Integer> {

    List<DigitalFile> findByOid(long oid);

    void deleteByOid(long oid);

}
