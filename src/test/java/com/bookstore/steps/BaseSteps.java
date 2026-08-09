package com.bookstore.steps;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSteps {

    private RequestSpecification requestSpecification;

    protected final Logger logger =
            LoggerFactory.getLogger(getClass());

    public void configureRequestSpecification(
            String baseUrl,
            String basePath) {

        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException(
                    "Base URL cannot be blank"
            );
        }

        if (basePath == null || basePath.isBlank()) {
            throw new IllegalArgumentException(
                    "Base path cannot be blank"
            );
        }

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setBasePath(basePath)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    protected RequestSpecification requestSpecification() {
        if (requestSpecification == null) {
            throw new IllegalStateException(
                    "Request specification has not been configured"
            );
        }

        return requestSpecification;
    }
}