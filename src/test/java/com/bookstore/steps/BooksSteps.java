package com.bookstore.steps;

import com.bookstore.model.books.Book;
import com.bookstore.model.books.BookRequest;
import io.restassured.response.Response;
import net.serenitybdd.annotations.Step;

import java.util.Arrays;
import java.util.List;

import static net.serenitybdd.rest.SerenityRest.given;

public class BooksSteps extends BaseSteps {

    private static final String BOOKS_PATH = "/Books";

    @Step("Get all books")
    public List<Book> getAllBooks() {
        Response response = given()
                .spec(requestSpecification())
            .when()
                .get(BOOKS_PATH);

        response.then().statusCode(200);

        return Arrays.asList(
                response.as(Book[].class)
        );
    }

    @Step("Get book with ID {0}")
    public Response getBookById(Integer bookId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", bookId)
            .when()
                .get(BOOKS_PATH + "/{id}");
    }

    @Step("Get book with invalid ID {0}")
    public Response getBookByInvalidId(Object bookId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", bookId)
            .when()
                .get(BOOKS_PATH + "/{id}");
    }

    @Step("Get book with ID null")
    public Response getBookByNullId() {
        return given()
                .spec(requestSpecification())
            .when()
                .get(BOOKS_PATH + "/null");
    }

    @Step("Create a book")
    public Response createBook(BookRequest request) {
        return given()
                .spec(requestSpecification())
                .body(request)
            .when()
                .post(BOOKS_PATH);
    }

    @Step("Update book with ID {0}")
    public Response updateBook(
            Integer bookId,
            BookRequest request) {

        return given()
                .spec(requestSpecification())
                .pathParam("id", bookId)
                .body(request)
            .when()
                .put(BOOKS_PATH + "/{id}");
    }

    @Step("Delete book with ID {0}")
    public Response deleteBook(Integer bookId) {
        return given()
                .spec(requestSpecification())
                .pathParam("id", bookId)
            .when()
                .delete(BOOKS_PATH + "/{id}");
    }
}