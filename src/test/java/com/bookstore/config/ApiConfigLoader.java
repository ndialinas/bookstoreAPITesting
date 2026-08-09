package com.bookstore.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ApiConfigLoader {

    private static final String CONFIG =
            "serenity.properties";

    private ApiConfigLoader() {
    }

    public static ApiConfig load() {
        Properties properties = new Properties();

        try (InputStream inputStream =
                     ApiConfigLoader.class
                             .getClassLoader()
                             .getResourceAsStream(CONFIG)) {

            if (inputStream == null) {
                throw new IllegalStateException(
                        "Missing resource: " + CONFIG
                );
            }

            properties.load(inputStream);

        } catch (IOException exception) {
            throw new IllegalStateException(
                    "Could not load " + CONFIG,
                    exception
            );
        }

        return new ApiConfig(
                resolveBaseUrl(properties),
                required(properties, "api.base-path")
        );
    }

    //Reads for enviromental variable as baseURL. If not found uses the default in the properties file
    private static String resolveBaseUrl(
            Properties properties) {

        String systemBaseUrl =
                System.getProperty("baseUrl");

        if (systemBaseUrl != null &&
                !systemBaseUrl.isBlank()) {
            return systemBaseUrl;
        }

        return required(properties, "api.base-url");
    }

    private static String required(
            Properties properties,
            String key) {

        String value = properties.getProperty(key);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Missing property: " + key
            );
        }

        return value;
    }
}