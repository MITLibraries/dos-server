package edu.mit.dos.persistence;

import edu.mit.dos.model.File;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FileJpaRepository extends JpaRepository<File, Integer> {

    File findByOid(int oid);

}
