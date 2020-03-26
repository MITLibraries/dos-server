package edu.mit.dos.object;

import edu.mit.dos.model.DigitalFile;
import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.model.DigitalObjectBuilder;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import edu.mit.dos.persistence.FileJpaRepository;
import edu.mit.dos.persistence.ObjectFilePersistence;
import edu.mit.dos.storage.StorageInterfaceFactory;
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

import static org.apache.commons.lang3.StringUtils.isEmpty;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Transactional
@RestController
public class ObjectService {

    private static final Logger logger = LoggerFactory.getLogger(ObjectService.class);
    public static final String OK = "ok";
    public static final String FAIL = "fail";

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
            final File file = persistToTemp(target);
            files.put(key, file);
        } catch (IOException e) {
            logger.error("Error creating file:{}", e);
        }

        try {
            final List<String> storagePaths = persistToStorage(files);
            final DigitalObject p = objectFilePersistence.save(storagePaths, object);
            return String.valueOf(p.postResponse());
        } catch (IOException e) {
            logger.error("Error:", e);
            return FAIL; //TODO update
        }
    }

    @RequestMapping(value = "/object", method = RequestMethod.DELETE)
    public void deleteObject(@RequestParam("oid") String oid) {
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.valueOf(oid));
        if (retrievedDigitalObject == null) {
            logger.debug("Error - digital object not found:{}", oid);
            return;
        }
        objectJpaRepository.delete(retrievedDigitalObject);
        final List<DigitalFile> filesToDelete = retrievedDigitalObject.getFiles();
        for (DigitalFile fileToDelete : filesToDelete) {
            storage.getInstance().deleteObject(fileToDelete.getPath());
            logger.debug("File deleted :{}", fileToDelete.getPath());
        }
        logger.debug("Object deleted:{}", oid);
    }


    @RequestMapping(value = "/object", method = RequestMethod.GET)
    public @ResponseBody
    DigitalObject getObject(@RequestParam("oid") String oid) {
        logger.debug("Finding oid:{}", oid);
        final DigitalObject retrievedDigitalObject = objectJpaRepository.findByOid(Long.parseLong(oid));
        if (retrievedDigitalObject == null) {
            logger.debug("Error - digital object not found:{}", oid);
            return new DigitalObject(); // todo replace with status code
        }
        logger.debug(retrievedDigitalObject.toString());
        return retrievedDigitalObject;
    }


    @RequestMapping(value = "/object", method = RequestMethod.PATCH)
    public String update(@RequestParam("oid") String oidStr,
                         @RequestParam(value = "handle", required = false, defaultValue = "") String handle,
                         @RequestParam(value = "title", required = false, defaultValue = "") String title,
                         @RequestParam(value = "content_source", required = false, defaultValue = "") String contentSource,
                         @RequestParam(value = "metadata_source", required = false, defaultValue = "") String metadataSystem,
                         @RequestParam(value = "file", required = false) MultipartFile targetFile) {

        logger.debug("Updating for object id:{}", oidStr);

        long oid = Long.parseLong(oidStr);

        DigitalObject object = objectJpaRepository.findByOid(oid);

        if (!isEmpty(handle)) {
            object.setHandle(handle);
        }

        if (!isEmpty(title)) {
            object.setTitle(title);
        }

        if (!isEmpty(contentSource)) {
            object.setContentSource(contentSource);
        }

        if (!isEmpty(metadataSystem)) {
            object.setMetadataSource(metadataSystem);
        }

        // if no file was file, that's fine - just save the updated object

        if (targetFile == null || targetFile.isEmpty()) {
            logger.debug("No file supplied for updating oid:{}", oid);
            try {
                object.setDateUpdated(new Date());
                object = objectJpaRepository.save(object);
                logger.debug("Saved object:{}", object);
                return OK;
            } catch (Exception e) {
                logger.error("Error updating object:", e);
                return FAIL;
            }
        }

        // if a file was supplied, associate the file with the object

        final String key = identifierFactory.getInstance().generate();

        try {

            // copy the file locally

            final File file = persistToTemp(targetFile);

            final Map<String, File> fileMap = new HashMap<>();

            fileMap.put(key, file);

            // delete existing associated files:

            logger.debug("Object id:{} has associated files, which will be deleted:{} ", oid, object.getFiles());

            List<DigitalFile> initFiles = new ArrayList<>();
            object.setFiles(initFiles);
            objectJpaRepository.save(object);

            logger.debug("Object id:{} files:{} ", oid, object.getFiles());

            // persist new file to permanent storage (local or s3):

            final List<String> results = persistToStorage(fileMap);

            // Finally update the database:

            final DigitalObject p = objectFilePersistence.save(results, object);
            logger.debug("Updated entity:{}", p);
            return OK;
        } catch (IOException e) {
            logger.error("Error:", e);
            return FAIL;
        }

    }

    private File persistToTemp(@RequestParam(value = "file", required = false) MultipartFile target) throws IOException {
        final File file = File.createTempFile("dos-", "-ingest");
        FileUtils.copyInputStreamToFile(target.getInputStream(), file);
        return file;
    }

    private List<String> persistToStorage(final Map<String, File> map) throws IOException {

        final List<String> results = new ArrayList<>();

        for (final Map.Entry<String, File> entry : map.entrySet()) {
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