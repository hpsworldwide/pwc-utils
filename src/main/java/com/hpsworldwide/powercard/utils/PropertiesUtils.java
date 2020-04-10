package com.hpsworldwide.powercard.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author (c) HPS Solutions
 */
public class PropertiesUtils {

    public static Properties loadFileFromEnvironmentVariable(String environmentVariable) throws IOException {
        String filePath = System.getenv(environmentVariable);
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("value of environment variable [" + environmentVariable + "] is empty or null: \"" + filePath + "\"");
        }
        return loadFromFile(new File(filePath));
    }

    public static Properties loadFromFile(File file) throws IOException {
        if (!file.exists() || !file.isFile()) {
            throw new IllegalArgumentException("file [" + file.getAbsolutePath() + "] does not exist or is not a file (directory?)");
        }
        try (InputStream inputStream = new FileInputStream(file)) {
            return loadFromInputStream(inputStream);
        }
    }

    public static Properties loadFromResource(String resourceName) throws IOException {
        try (InputStream inputStream = PropertiesUtils.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (inputStream != null) {
                return loadFromInputStream(inputStream);
            } else {
                throw new FileNotFoundException("properties resource [" + resourceName + "] not found");
            }
        }
    }

    public static Properties loadFromInputStream(InputStream inputStream) throws IOException {
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }

}
