package com.ctflearn.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private static final Logger logger = LogManager.getLogger(ConfigManager.class.getSimpleName());
    private Properties properties;
    private InputStream inputStream;

    public ConfigManager() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        loadProperty("config.properties");
        loadProperty(properties.getProperty("environment") + ".properties");
        loadProperty(properties.getProperty("mobilePlatform") + ".properties");
    }

    private void loadProperty(String propertyName) {
        try {
            inputStream = getInputStream(propertyName, null);
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            logger.error("Exception while reading {} file...", propertyName, e);
        }
    }

    private InputStream getInputStream(String fileName, String filePath) {
        try {
            if (filePath == null) {
                filePath = System.getProperty("user.dir") + File.separator + "configs" + File.separator;
            }
            return new FileInputStream(filePath + fileName);
        } catch (FileNotFoundException e) {
            return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        }
    }

    public String getValueOfProperty(String propertyName) {
        return properties.getProperty(propertyName);
    }
}
