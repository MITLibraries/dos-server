package edu.mit.dos.storage;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class LocalFileSystemImpl implements StorageManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileSystemImpl.class);

    private static File tempDirPath;

    @PostConstruct
    public void generate() {
        try {
            tempDirPath = createTempDirectory();
        } catch (IOException e) {
            logger.error("Error writing to disk:", e);
        }
    }

    @Override
    public String putObject(String key, File file) {
        try {
            final File f = new File( tempDirPath + File.separator + "dos" + key);
            FileUtils.copyFile(file, f); // copies file to specific path on disk
            logger.debug("Copied to:{}", f.getAbsolutePath());
            return f.getPath();
        } catch (IOException e) {
            logger.error("Error copying file:", e);
        }
        return "";
    }

    @Override
    public void deleteObject(String key) {
        try {
            File f = new File(key);
            f.delete();
        } catch (Exception e) {
            logger.error("Error deleting file", e);
        }
    }

    @Override
    public String getObject(String key) {
        return key;
    }

    public static File createTempDirectory() throws IOException {
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
