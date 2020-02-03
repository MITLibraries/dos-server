package edu.mit.dos.storage;

import java.io.File;

public interface StorageManager {

    String putObject(String key, File file);

    void deleteObject(String key);

}
