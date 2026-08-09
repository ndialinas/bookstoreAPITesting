package com.bookstore.tests;

import com.bookstore.config.ApiConfig;
import com.bookstore.config.ApiConfigLoader;
import com.bookstore.steps.AuthorsSteps;
import com.bookstore.steps.BooksSteps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.core.Serenity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(SerenityJUnit5Extension.class)
public abstract class ApiTestBase {

    protected ApiConfig apiConfig;
    
    protected final Logger logger =
            LoggerFactory.getLogger(getClass());

    @Steps
    protected BooksSteps booksSteps;

    @Steps
    protected AuthorsSteps authorsSteps;

    protected void attachLogToReport() throws IOException {
        Path logFile = Path.of("target", "logs", "serenity-tests.log");

        if (Files.exists(logFile)) {
            Serenity.recordReportData()
                    .asEvidence()
                    .withTitle("Execution log")
                    .fromFile(logFile);

            logger.info("Attached log file to Serenity report: {}", logFile);
        } else {
            logger.warn("Log file does not exist: {}", logFile);
        }
    }

    @BeforeEach
    void configureSteps() {
        apiConfig = ApiConfigLoader.load();

        booksSteps.configureRequestSpecification(
                apiConfig.baseUrl(),
                apiConfig.basePath()
        );

        authorsSteps.configureRequestSpecification(
                apiConfig.baseUrl(),
                apiConfig.basePath()
        );
    }

    @AfterEach
    void attachExecutionLog() throws IOException {
        attachLogToReport();
    }
}