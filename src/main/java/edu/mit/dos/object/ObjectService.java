package edu.mit.dos.object;

import edu.mit.dos.model.DigitalFile;
import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import edu.mit.dos.persistence.FileJpaRepository;
import edu.mit.dos.storage.StorageInterfaceFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@RestController
public class ObjectService {

    private static final Logger logger = LoggerFactory.getLogger(ObjectService.class);

    @Autowired
    private DigitalObjectJpaRepository objectJpaRepository;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    @Autowired
    private StorageInterfaceFactory s3Manager;

    @RequestMapping(value = "/object", method = RequestMethod.POST)
    public String create(@RequestParam("handle") String handle,
                         @RequestParam("title") String title,
                         @RequestParam("target_links") List<String> targetLinks,
                         @RequestParam("source_system") String sourceSystem,
                         @RequestParam("metadata_system") String metadataSystem) {

        final DigitalObject obj = new DigitalObject();
        obj.setHandle(handle);
        obj.setTitle(title);
        obj.setDateCreated(new Date());
        obj.setUpdateDate(new Date());
        obj.setMetadataSource(metadataSystem);
        obj.setSourceSystem(sourceSystem);

        int num = 0; // TODO

        for (final String s : targetLinks) { //TODO download/stream to S3

            // Persist to S3 or disk depending on what was passed at runtime

            try {
                final File f = new File(createTempDirectory() + "/" + num);

                URL uri = new URL(s);
                FileUtils.copyURLToFile(uri, f); // downloads first

                final String key = obj.getHandle() + "/" + num;
                num++;

                // Save to disk:

                final String result  = s3Manager.getInstance().putObject(key, f); // no need to download it?

                logger.debug("DigitalFile:{} persisted with path:{}", s, result);

                logger.debug("Temporary file:{} on disk deleted:{}", f.getPath(), f.delete());

                // Save to db:

                final DigitalFile digitalFile = new DigitalFile();
                digitalFile.setPath(result); // Construct path out of saved S3
                fileJpaRepository.save(digitalFile);
                final List<DigitalFile> digitalFiles = obj.getFiles();
                digitalFiles.add(digitalFile); //todo clean up
                obj.setFiles(digitalFiles);
                logger.debug("DigitalFile copied to path:{}", result);

            } catch (Exception e) {
                logger.error("Error writing file:", e);
            }
        }

        final DigitalObject persistedObjected = objectJpaRepository.save(obj);
        logger.debug("Persisted:{}", persistedObjected.getOid());
        return String.valueOf(persistedObjected.getOid());
    }


    @RequestMapping(value = "/object", method = RequestMethod.GET)
    public @ResponseBody
    DigitalObject getObject(@RequestParam("oid") String oid) {
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.valueOf(oid));
        if (retrievedDigitalObject == null){
            logger.debug("Error - digital object not found:{}", oid);
        }
        logger.debug(retrievedDigitalObject.toString());
        return retrievedDigitalObject;
    }

    public static File createTempDirectory()
            throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }


}