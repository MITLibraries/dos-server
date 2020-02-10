package edu.mit.dos.util;

import edu.mit.dos.service.UserServiceImpl;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileConverter {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private static String PREFIX = "temp";

    public static File toFile(final String httpLink) throws IOException {
        final File f = new File(createTempFile().getPath() + "/" + RandomStringUtils.randomAlphanumeric(12));

        if (f.exists()) {
            return null;
        }

        final URL uri = new URL(httpLink);
        try {
            FileUtils.copyURLToFile(uri, f);
        } catch (IOException e) {
            logger.error("Error copying file to url. {}", e);
            throw e;
        }
        return f;
    }

    private static File createTempFile() throws IOException {
        final File temp = File.createTempFile(PREFIX, Long.toString(System.nanoTime())); // todo create per request

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }
}
