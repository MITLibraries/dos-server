package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalObject;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DigitalObjectJpaRepository extends JpaRepository<DigitalObject, Integer> {

    DigitalObject findByOid(long oid);

    DigitalObject findByHandle(String handle);

}
