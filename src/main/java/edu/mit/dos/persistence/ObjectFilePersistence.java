package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalObject;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ObjectFilePersistence {

    @Transactional
    DigitalObject save(List<String> storagePaths, DigitalObject object);

}
