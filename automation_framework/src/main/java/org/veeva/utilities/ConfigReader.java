package org.veeva.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties prop = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("configuration/config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties file not found in resources/configuration folder");
            }

            prop.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file", e);
        }
    }

    public static String getProperty(String key) {
        return prop.getProperty(key);
    }
}
