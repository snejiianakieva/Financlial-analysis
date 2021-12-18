package com.ers.internship.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Snejina Yanakieva
 */
public class PropertyManager {

    private static final Logger logger = LoggerFactory.getLogger(PropertyManager.class);

    private final Map<String, String> propertiesMap;
    private boolean validity;

    PropertyManager(Properties properties, String... propertyList) {
        propertiesMap = new HashMap<>();
        validity = true;
        for (String property : propertyList) {
            String loaded = properties.getProperty(property);
            if (validateString(property, loaded)) {
                propertiesMap.put(property, loaded);
            }
        }
    }

    public String getProperty(String property) {
        return propertiesMap.get(property);
    }

    public boolean getValidity() {
        return validity;
    }

    private boolean validateString(String message, String property) {
        if (property == null) {
            logger.error(message + " property not found");
            validity = false;
            return false;
        }
        logger.info(message + " = " + property);
        return true;
    }
}
