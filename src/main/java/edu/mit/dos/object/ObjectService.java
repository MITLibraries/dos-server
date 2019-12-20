package edu.mit.dos.object;

import edu.mit.dos.model.DigitalObject;
import edu.mit.dos.persistence.DigitalObjectJpaRepository;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

@RestController
public class ObjectService {

    private static final Logger logger = LoggerFactory.getLogger(ObjectService.class);

    @Autowired
    private DigitalObjectJpaRepository objectJpaRepository;

    @RequestMapping(value = "/object", method = RequestMethod.POST)
    public String create(@RequestParam("handle") String handle,
                         @RequestParam("uris") List<String> fileUris,
                         @RequestParam("title") String title,
                         @RequestParam("source_system") String sourceSystem,
                         @RequestParam("metadata_system") String metadataSystem) {

        for (final String s : fileUris) {

            int filesWritten = 0;

            final URL url;
            try {
                url = new URL(s);
            } catch (MalformedURLException e) {
                logger.error("Error with url:", e);
                break;
            }


            try {
                File f = new File( createTempDirectory() + File.separator + "dos" + handle + File.separator + filesWritten);
                FileUtils.copyURLToFile(url, f);
                logger.debug("File copied to path:{}", f.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Error copying file to URL:", e);
            }
        }

        final DigitalObject digitalObject = new DigitalObject();
        digitalObject.setHandle(handle);
        digitalObject.setUpdateDate(new Date());
        digitalObject.setMetadataSource(metadataSystem);
        digitalObject.setMetadataSourceUrl(sourceSystem);

        final DigitalObject persistedObjected = objectJpaRepository.save(digitalObject);

        logger.debug("Persisted:{}", persistedObjected.getOid());

        return "ok";
    }


    @RequestMapping(value = "/object", method = RequestMethod.GET)
    public @ResponseBody
    String getObject(@RequestParam("oid") String objectId) {
        return "ok";
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
