package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileJpaRepository extends JpaRepository<DigitalFile, Integer> {

    DigitalFile findByFid(long fid);

    void deleteByFid(long fid);

}
