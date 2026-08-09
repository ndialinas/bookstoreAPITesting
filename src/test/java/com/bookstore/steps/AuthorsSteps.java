package com.bookstore.steps;

import com.bookstore.model.authors.Author;
import com.bookstore.model.authors.AuthorRequest;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;
import net.serenitybdd.annotations.Step;

import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.rest.SerenityRest.given;

@Slf4j
public class AuthorsSteps extends BaseSteps {

    private static final String AUTHORS_PATH = "/Authors";
    private static final String BOOK_AUTHORS_PATH = AUTHORS_PATH + "/authors/books/";

    @Step("Get all authors")
    public List<Author> getAllAuthors() {
        Response response = given()
                .spec(requestSpecification())
            .when()
                .get(AUTHORS_PATH);

        response.then().statusCode(200);

        return Arrays.asList(
                response.as(Author[].class)
        );
    }

    @Step("Get author with ID {0}")
    public Response getAuthorById(Integer authorId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", authorId)
            .when()
                .get(AUTHORS_PATH + "/{id}");
    }

    @Step("Get author with invalid ID {0}")
    public Response getAuthorByInvalidId(Object authorId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", authorId)
            .when()
                .get(AUTHORS_PATH + "/{id}");
    }

    @Step("Get author with ID null")
    public Response getAuthorWithIdNull() {
        return given()
                .spec(requestSpecification())
            .when()
                .get(AUTHORS_PATH + "/null");
    }

    @Step("Get author of book with book ID {0}")
    public Response getAuthorsOfBookById(Integer bookId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", bookId)
            .when()
                .get(BOOK_AUTHORS_PATH + "/{id}");
    }

    @Step("Get author of book with invalid book ID {0}")
    public Response getAuthorsOfBookByInvalidId(Object bookId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", bookId)
            .when()
                .get(BOOK_AUTHORS_PATH + "/{id}");
    }

    @Step("Create an author")
    public Response createAuthor(AuthorRequest request) {
        log.info("Creating author with ", request);
        return given()
                .spec(requestSpecification())
                .body(request)
            .when()
                .post(AUTHORS_PATH);
    }

    @Step("Try to create an author with invalid values")
    public Response createAuthorInvalidValues(Object invalidRequest) {
        return given()
                .spec(requestSpecification())
                .body(invalidRequest)
            .when()
                .post(AUTHORS_PATH);
    }

    @Step("Update author with ID {0}")
    public Response updateAuthor(
            Integer authorId,
            AuthorRequest request) {
        log.info("Updating author with {0} {1}", authorId, request);
        return given()
                .spec(requestSpecification())
                .pathParam("id", authorId)
                .body(request)
            .when()
                .put(AUTHORS_PATH + "/{id}");
    }

    @Step("Try to update author without an ID")
    public Response updateAuthorWithoutId(AuthorRequest request) {
        return given()
            .spec(requestSpecification())
            .body(request)
        .when()
            .put(AUTHORS_PATH + "/");
    }


    @Step("Try to update author without an ID of null")
    public Response updateAuthorWithIdNull(AuthorRequest request) {
        return given()
            .spec(requestSpecification())
            .body(request)
        .when()
            .put(AUTHORS_PATH + "/null");
    }

    @Step("Delete author with ID {0}")
    public Response deleteAuthor(Integer authorId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", authorId)
            .when()
                .delete(AUTHORS_PATH + "/{id}");
    }

    @Step("Delete author with invalid ID {0}")
    public Response deleteAuthorWithInvalidId(Object authorId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", authorId)
            .when()
                .delete(AUTHORS_PATH + "/{id}");
    }
}