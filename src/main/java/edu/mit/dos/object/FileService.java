package edu.mit.dos.object;

import edu.mit.dos.ServiceConfig;
import edu.mit.dos.model.DigitalFile;
import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import edu.mit.dos.persistence.FileJpaRepository;
import edu.mit.dos.persistence.ObjectFilePersistence;
import edu.mit.dos.storage.StorageInterfaceFactory;
import edu.mit.dos.util.FileConverter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

@Transactional
@RestController
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    @Autowired
    private DigitalObjectJpaRepository objectJpaRepository;

    @Autowired
    private StorageInterfaceFactory storage;

    @Autowired
    private ServiceConfig serviceConfig;

    /**
     * Retrieves files by oid
     *
     * @param oid
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/file", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam("oid") String oid) throws IOException {
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.valueOf(oid));

        if (retrievedDigitalObject == null) {
            logger.debug("Error - digital object not found:{}", oid);
        } else {
            logger.debug(retrievedDigitalObject.toString());
        }

        if (retrievedDigitalObject.getFiles() == null) {
            logger.error("No associated files for oid:{}", oid);
            return null;
        }

        final DigitalFile file = retrievedDigitalObject.getFiles().get(0); // TODO there can be more files

        logger.debug("Retrieving:{}", file.getPath());

        final String key = file.getPath().replace(serviceConfig.getBaseurl(), "");

        logger.debug("Key:{}", key);

        final String path = storage.getInstance().getObject(key);

        logger.debug("Obtaining file from local area:{}", path);

        final InputStream in = FileUtils.openInputStream(new File(path));
        return IOUtils.toByteArray(in);

    }

}