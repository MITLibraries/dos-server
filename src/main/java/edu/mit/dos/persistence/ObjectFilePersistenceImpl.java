package edu.mit.dos.persistence;

import edu.mit.dos.model.DigitalFile;
import edu.mit.dos.model.DigitalObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ObjectFilePersistenceImpl implements ObjectFilePersistence {

    private static final Logger logger = LoggerFactory.getLogger(ObjectFilePersistenceImpl.class);

    @Autowired
    private DigitalObjectJpaRepository objectJpaRepository;

    @Autowired
    private FileJpaRepository fileJpaRepository;


    @Transactional
    public DigitalObject save(final List<String> storagePaths, final DigitalObject object) {

        for (final String result: storagePaths) {
            final DigitalFile digitalFile = new DigitalFile();
            digitalFile.setPath(result);
            fileJpaRepository.save(digitalFile);
            object.getFiles().add(digitalFile);
        }

        final DigitalObject savedObject = objectJpaRepository.save(object);
        logger.debug("Saved:{}", savedObject);
        return savedObject;
    }
}
