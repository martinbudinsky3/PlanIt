package com.example.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
    InputStream inputStream;
    String property = "";
    String propFileName;

    public PropertiesReader(String propFileName) {
        this.propFileName = propFileName;
    }

    public String getProperty(String propertyKey){
        try {
            Properties prop = new Properties();

            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);

            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }

            // get the property value and print it out
            property = prop.getProperty(propertyKey);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
        return property;
    }
}
