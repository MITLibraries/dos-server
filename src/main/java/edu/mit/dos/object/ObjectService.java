package edu.mit.dos.object;

import edu.mit.dos.model.DigitalFile;
import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.model.DigitalObjectBuilder;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import edu.mit.dos.persistence.FileJpaRepository;
import edu.mit.dos.persistence.ObjectFilePersistence;
import edu.mit.dos.storage.StorageInterfaceFactory;
import edu.mit.dos.util.FileConverter;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.*;

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

    @Autowired
    private ObjectFilePersistence objectFilePersistence;

    @RequestMapping(value = "/object", method = RequestMethod.POST)
    public String create(@RequestParam(value = "handle", required = false) String handle,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "content_source", required = false) String contentSource,
                         @RequestParam(value = "metadata_source", required = false) String metadataSystem,
                         @RequestParam(value = "file", required = false) MultipartFile target) {

        final DigitalObject object = new DigitalObjectBuilder()
                .setHandle(handle)
                .setTitle(title)
                .setDateCreated(new Date())
                .setDateUpdated(new Date())
                .setMetadataSource(metadataSystem)
                .setContentSource(contentSource)
                .createDigitalObject();

        // copy the files locally, then copy the files to storage, and then persist to the database:

        final Map<String, File> files = new HashMap<>(); // <key, file>
        final String key = identifierFactory.getInstance().generate(); // + File.separator + item;

        try {
            final File file = File.createTempFile("dos-", "-ingest");
            FileUtils.copyInputStreamToFile(target.getInputStream(), file);
            files.put(key, file);
        } catch (IOException e) {
            logger.error("Error creating file:{}", e);
        }

        try {
            final List<String> storagePaths = persistToStorage(files);
            final DigitalObject p = objectFilePersistence.save(storagePaths, object);
            return String.valueOf(p.postResponse());
        } catch (IOException e) {
            logger.error("Error:{}", e);
            return "fail"; //TODO update
        }
    }

    @RequestMapping(value = "/object", method = RequestMethod.DELETE)
    public void deleteObject(@RequestParam("oid") String oid) {
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.valueOf(oid));
        if (retrievedDigitalObject == null){
            logger.debug("Error - digital object not found:{}", oid);
        }
        objectJpaRepository.delete(retrievedDigitalObject);
        final List<DigitalFile> filesToDelete = retrievedDigitalObject.getFiles();
        for (DigitalFile fileToDelete : filesToDelete){
            storage.getInstance().deleteObject(fileToDelete.getPath());
            logger.debug("File deleted :{}", fileToDelete.getPath());
        }
        logger.debug("Object deleted:{}", oid);
    }


    @RequestMapping(value = "/object", method = RequestMethod.GET)
    public @ResponseBody
    DigitalObject getObject(@RequestParam("oid") String oid) {
        logger.debug("Finding oid:{}", oid);
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.valueOf(oid));
        if (retrievedDigitalObject == null) {
            logger.debug("Error - digital object not found:{}", oid);
        }
        logger.debug(retrievedDigitalObject.toString());
        return retrievedDigitalObject;
    }


    @RequestMapping(value = "/object", method = RequestMethod.PATCH)
    public String update(@RequestParam("oid") String oidStr,
                         @RequestParam(value = "handle", required = false) String handle,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "target_links", required = false) List<String> targetLinks,
                         @RequestParam(value = "content_source", required = false) String contentSource,
                         @RequestParam(value = "metadata_source", required = false) String metadataSystem) {

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

        object.setDateUpdated(new Date());

        if (!contentSource.isEmpty()) { //tbd can do validation here. if so move to signature with the annotation
            object.setContentSource(contentSource);
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
            return "fail"; //TODO
        }

        logger.debug("Deleted Files. Current files:{}", fileJpaRepository.findByOid(oid));

        // first copy the files, then copy the files to storage -- kind of like a block chain effect

        final Map<String, File> map =new HashMap<>();

        for (final String s : targetLinks) {
            try {
                final File f = FileConverter.toFile(s);
                final String key = object.getHandle(); // + "/" + item;
                map.put(key, f);
            } catch (IOException e) {
                logger.error("Error downloading files:{}", e);
                return "error"; // todo
            }
        }


        try {
            final List<String> results = persistToStorage(map);

            // Update the database:
            final DigitalObject p = objectFilePersistence.save(results, object);

            logger.debug("Updated entity:{}", p);

            return "ok"; // todo
        } catch (IOException e) {
            logger.error("Error:", e);
            return "fail";
        }
    }

    private List<String> persistToStorage(final Map<String, File> map) throws IOException {

        final List<String> results = new ArrayList<>();

        for (final Map.Entry<String,File> entry : map.entrySet()) {
            final String path = entry.getKey();
            final File f = entry.getValue();

            if (!f.exists()) {
                logger.warn("File no longer exists:{}", f.getPath());
                continue;
            }

            // Persist to storage:

            final String result;
            try {
                result = storage.getInstance().putObject(path, f);
            } catch (IOException e) {
                logger.error("Error writing to storage:", e);
                throw e;
            }

            logger.debug("DigitalFile:{} persisted to storage path:{}", path, f);

            results.add(result);
        }

        return results;
    }


}