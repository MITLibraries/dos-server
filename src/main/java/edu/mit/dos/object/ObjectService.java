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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Transactional
@RestController
public class ObjectService {

    private static final Logger logger = LoggerFactory.getLogger(ObjectService.class);

    @Autowired
    private DigitalObjectJpaRepository objectJpaRepository;

    @Autowired
    private FileJpaRepository fileJpaRepository;

    @Autowired
    private StorageInterfaceFactory storage;

    @Autowired
    private IdentifierFactory identifierFactory;

    @RequestMapping(value = "/object", method = RequestMethod.POST)
    public String create(@RequestParam("handle") String handle,
                         @RequestParam("title") String title,
                         @RequestParam("target_links") List<String> targetLinks,
                         @RequestParam("source_system") String sourceSystem,
                         @RequestParam("metadata_system") String metadataSystem) {

        final DigitalObject object = new DigitalObject();
        object.setHandle(handle);
        object.setTitle(title);
        object.setDateCreated(new Date());
        object.setUpdateDate(new Date());
        object.setMetadataSource(metadataSystem);
        object.setSourceSystem(sourceSystem);

        int item = 0;

        for (final String s : targetLinks) {

            try {  // todo refactor all file logic out
                final File f = new File(createTempDirectory() + File.separator + item);

                final URL uri = new URL(s);
                FileUtils.copyURLToFile(uri, f); // downloads first

                logger.debug("Written file:{}", f.getAbsolutePath());

                final String key = identifierFactory.getInstance().generate() + File.separator + item;

                logger.debug("Using identifier:{}", key);

                item++;

                // Save to storage (s3 or disk, depending on what's specified in the properties file):

                final String result = storage.getInstance().putObject(key, f);

                logger.debug("DigitalFile:{} persisted with path:{}", s, result);

                // Save to the database:

                final DigitalFile file = new DigitalFile();
                file.setPath(result);
                fileJpaRepository.save(file);

                final List<DigitalFile> digitalFiles = object.getFiles();
                digitalFiles.add(file); //todo clean up (refactor all file logic logic out)
                object.setFiles(digitalFiles);

                // Delete the temporary file:

                logger.debug("Temporary file:{} deleted:{}", f.getPath(), f.delete());

            } catch (IOException e) {
                logger.error("I/O error with file operation:", e);
            }
        }

        logger.debug("Files copied:{} out of:", item, targetLinks.size());

        final DigitalObject savedObject = objectJpaRepository.save(object);
        logger.debug("Persisted to database:{}", savedObject.getOid());
        return String.valueOf(savedObject.getOid());
    }


    @RequestMapping(value = "/object", method = RequestMethod.GET)
    public @ResponseBody
    DigitalObject getObject(@RequestParam("oid") String oid) {
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.valueOf(oid));
        if (retrievedDigitalObject == null) {
            logger.debug("Error - digital object not found:{}", oid);
        }
        logger.debug(retrievedDigitalObject.toString());
        return retrievedDigitalObject;
    }

    private File createTempDirectory() throws IOException {
        final File temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }

    @RequestMapping(value = "/object", method = RequestMethod.PATCH)
    public String update(@RequestParam("oid") String oidStr,
                         @RequestParam(value = "handle", required = false) String handle,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "target_links", required = false) List<String> targetLinks,
                         @RequestParam(value = "source_system", required = false) String sourceSystem,
                         @RequestParam(value = "metadata_system", required = false) String metadataSystem) {

        logger.debug("Updating for object id:{}", oidStr);

        long oid = Long.parseLong(oidStr);

        DigitalObject object = objectJpaRepository.findByOid(oid);

        if (!handle.isEmpty()) {
            object.setHandle(handle);
        } else {
            return "Invalid attribute (handle) supplied for PATCH";
        }

        if (!title.isEmpty()) {
            object.setTitle(title);
        } else {
            return "Invalid attribute (title) supplied for PATCH";
        }

        object.setUpdateDate(new Date());

        if (!sourceSystem.isEmpty()) { //tbd can do validation here. if so move to signature with the annotation
            object.setSourceSystem(sourceSystem);
        } else {
            return "Invalid attribute (source) supplied for PATCH";
        }

        if (!metadataSystem.isEmpty()) {
            object.setMetadataSource(metadataSystem);
        } else {
            return "Invalid attribute (metadata) supplied for PATCH";
        }

        if (!targetLinks.isEmpty()) {
            List<DigitalFile> empty = new ArrayList<>();
            object.setFiles(empty);
        }

        try {
            object = objectJpaRepository.save(object);
        } catch (Exception e) {
            logger.error("Error updating object:", e);
            return "fail"; // todo change to status code
        }

        final List<DigitalFile> files = fileJpaRepository.findByOid(oid);

        logger.debug("Existing files:{}", files, "for oid:{}", oid);

        try {
            fileJpaRepository.deleteByOid(oid);
        } catch (Exception e) {
            logger.error("Error deleting files for oid:{}", oid);
            return "fail";
        }

        logger.debug("Deleted Files. Current files:{}", fileJpaRepository.findByOid(oid));

        // TODO The following block will be refactored/removed once DOS-176 is merged. Currently this is duplicate code.

        int num = 0;

        for (final String s : targetLinks) {

            try {
                final File f = new File(createTempDirectory() + "/" + num);

                final URL uri = new URL(s);
                FileUtils.copyURLToFile(uri, f); // downloads first

                logger.debug("Written file:{}", f.getAbsolutePath());

                final String key = object.getHandle() + "/" + num;
                num++;

                // Save to disk:

                final String result = s3Manager.getInstance().putObject(key, f); // no need to download it?

                logger.debug("DigitalFile:{} persisted with path:{}", s, result);

                logger.debug("Temporary file:{} on disk deleted:{}", f.getPath(), f.delete());

                // Save to db:

                final DigitalFile digitalFile = new DigitalFile();
                digitalFile.setPath(result); // Construct path out of saved S3
                fileJpaRepository.save(digitalFile);

                final List<DigitalFile> digitalFiles = object.getFiles();
                digitalFiles.add(digitalFile); //todo clean up
                object.setFiles(digitalFiles);

                logger.debug("DigitalFile copied to path:{}", result);

            } catch (Exception e) {
                logger.error("Error writing file:", e);
            }
        }

        logger.debug("New files:{}", fileJpaRepository.findByOid(oid));

        objectJpaRepository.save(object);
        return "ok";
    }


}