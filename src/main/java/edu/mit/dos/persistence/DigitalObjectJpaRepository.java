package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalObject;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;



public interface DigitalObjectJpaRepository extends JpaRepository<DigitalObject, Integer> {

    DigitalObject findByOid(int oid);

    DigitalObject findByHandle(String handle);

}
