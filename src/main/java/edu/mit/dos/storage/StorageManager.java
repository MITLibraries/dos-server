package edu.mit.dos.storage;

import java.io.File;
import java.io.IOException;

public interface StorageManager {

    String putObject(String key, File file) throws IOException;

    void deleteObject(String key);

}
