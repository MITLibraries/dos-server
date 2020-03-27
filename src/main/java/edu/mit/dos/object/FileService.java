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
    private FileJpaRepository fileJpaRepository;

    @Autowired
    private StorageInterfaceFactory storage;

    @Autowired
    private ServiceConfig serviceConfig;

    /**
     * Retrieves files by fid
     *
     * @param fid the id of the Digital File that is used for retrieving it
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/file", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getImageWithMediaType(@RequestParam("fid") String fid) throws IOException {

        final DigitalFile file = fileJpaRepository.findByFid(Long.parseLong(fid));

        if (file == null) {
            logger.debug("Error - digital file not found:{}", fid);
            return null;
        } else {
            logger.debug(file.toString());
        }

        final String path = storage.getInstance().getObject(file.getPath());

        logger.debug("Obtaining file from local area:{}", path);

        final InputStream in = FileUtils.openInputStream(new File(path));
        return IOUtils.toByteArray(in);

    }

}