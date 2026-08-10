package com.bookstore.tests.authors;

import com.bookstore.data.authors.AuthorTestData;
import com.bookstore.data.books.BookTestData;
import com.bookstore.model.authors.Author;
import com.bookstore.model.authors.AuthorRequest;
import com.bookstore.tests.ApiTestBase;


import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Map;


@Slf4j
class AuthorsApiTest extends ApiTestBase {

    @Test
    void shouldRetrieveAllAuthors() {
        var authors = authorsSteps.getAllAuthors();
        assertThat(authors)
                .isNotNull()
                .isNotEmpty();

        assertThat(authors)
                .allSatisfy(author -> {
                    assertThat(author.id()).isNotNull();
                    assertThat(author.firstName()).isNotBlank();
                    assertThat(author.lastName()).isNotBlank();
                });
    }

    @Test
    void shouldRetrieveAuthorById() {
        Response response =
                authorsSteps.getAuthorById(
                        AuthorTestData.EXISTING_AUTHOR_ID
                );

        assertThat(response.statusCode())
                .isEqualTo(200);

        Author actualAuthor =
                response.as(Author.class);

        assertThat(actualAuthor.id())
                .isEqualTo(
                        AuthorTestData.EXISTING_AUTHOR_ID
                );
    }

    @Test
    void shouldRetrieveAuthorsOfBookById() {
        List<Author> expectedAuthors = List.of(
        Author.builder()
                .id(1)
                .idBook(1)
                .firstName("First Name 1")
                .lastName("Last Name 1")
                .build(),

        Author.builder()
                .id(2)
                .idBook(1)
                .firstName("First Name 2")
                .lastName("Last Name 2")
                .build(),

        Author.builder()
                .id(3)
                .idBook(1)
                .firstName("First Name 3")
                .lastName("Last Name 3")
                .build(),

        Author.builder()
                .id(4)
                .idBook(1)
                .firstName("First Name 4")
                .lastName("Last Name 4")
                .build()
);
        Response response =
                authorsSteps.getAuthorsOfBookById(
                        BookTestData.EXISTING_BOOK_ID
                );
        assertThat(response.statusCode()).isEqualTo(200);

        assertThat(getAuthors(response)).as("Authors returned").containsAnyElementsOf(expectedAuthors);
    }

    @Test
    void shouldRetrieveEmptyAuthorsOfBookByIdZero() {
        Response response =
                authorsSteps.getAuthorsOfBookById(0);
        
        assertThat(response.statusCode()).isEqualTo(200);

        log.info("Authors returned", getAuthors(response));

        assertThat(getAuthors(response)).as("Authors returned").isNullOrEmpty();
    
    }

    @Test
    void shouldNotRetrieveAuthorsOfBooksWhenBookIdIsString() {
        Response response =
                authorsSteps.getAuthorsOfBookByInvalidId("someId");

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("errors.idBook[0]"))
            .isEqualTo("The value 'someId' is not valid.");

    }

    
    @Test
    void shouldRejectCreatingAuthorWithInvalidIdValues() {
        Map<String, String> invalidAuthor = Map.of(
        "id", "3333333333333333333333333333333333333333333",
        "idBook", "3",
        "firstName", "First Name",
        "lastName", "Last Name"
        );
        
        Response response =
                authorsSteps.createAuthorInvalidValues(invalidAuthor);

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getList("errors.'$.id'", String.class).get(0))
            .contains("The JSON value could not be converted to System.Int32. Path: $.id");
    }

    
    @Test
    void shouldRejectCreatingAuthorWithInvalidBookIdValues() {
        Map<String, String> invalidAuthor = Map.of(
        "id", "3",
        "idBook", "3333333333333333333333333333333333333333333",
        "firstName", "First Name",
        "lastName", "Last Name"
        );
        
        Response response =
                authorsSteps.createAuthorInvalidValues(invalidAuthor);

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getList("errors.'$.idBook'", String.class).get(0))
            .contains("The JSON value could not be converted to System.Int32. Path: $.idBook");
    }

    @Test
    void shouldNotRetrieveAuthorWhenIdIsNull() {
        Response response =
                authorsSteps.getAuthorWithIdNull();

        assertThat(response.statusCode())
                .isEqualTo(400);

    }

    @Test
    void shouldNotRetrieveAuthorWhenIdIsString() {
        Response response =
                authorsSteps.getAuthorByInvalidId("someId");

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("errors.id[0]"))
            .isEqualTo("The value 'someId' is not valid.");

    }

    @Test
    void shouldCreateAuthor() {
        AuthorRequest expectedAuthor =
                AuthorTestData.basicPost();

        Response response =
                authorsSteps.createAuthor(expectedAuthor);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Author actualAuthor =
                response.as(Author.class);

        assertThat(actualAuthor.id())
                .isNotNull();
        assertThat(actualAuthor.firstName())
                .isEqualTo(expectedAuthor.firstName());
        assertThat(actualAuthor.lastName())
                .isEqualTo(expectedAuthor.lastName());
        assertThat(actualAuthor.idBook())
                .isEqualTo(0);
    }

     @Test
    void shouldCreateAuthorWithId() {
        AuthorRequest expectedAuthor =
                AuthorTestData.withId();

        Response response =
                authorsSteps.createAuthor(expectedAuthor);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Author actualAuthor =
                response.as(Author.class);
        
        assertThat(actualAuthor.id())
                .isEqualTo(expectedAuthor.id());
        assertThat(actualAuthor.firstName())
                .isEqualTo(expectedAuthor.firstName());
        assertThat(actualAuthor.lastName())
                .isEqualTo(expectedAuthor.lastName());
        assertThat(actualAuthor.idBook())
                .isEqualTo(0);
    }

    @Test
    void shouldCreateAuthorWithBookId() {
        AuthorRequest expectedAuthor =
                AuthorTestData.withBookId();

        Response response =
                authorsSteps.createAuthor(expectedAuthor);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Author actualAuthor =
                response.as(Author.class);

        assertThat(actualAuthor.id())
                .isNotNull();
        assertThat(actualAuthor.firstName())
                .isEqualTo(expectedAuthor.firstName());
        assertThat(actualAuthor.lastName())
                .isEqualTo(expectedAuthor.lastName());
        assertThat(actualAuthor.idBook())
                .isEqualTo(expectedAuthor.idBook());
    }



    @Test
    void shouldCreateAuthorWithoutLastName() {
        AuthorRequest expectedAuthor =
                AuthorTestData.withoutLastName();

        Response response =
                authorsSteps.createAuthor(expectedAuthor);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Author actualAuthor =
                response.as(Author.class);

        assertThat(actualAuthor.id())
                .isNotNull();
        assertThat(actualAuthor.firstName())
                .isEqualTo(expectedAuthor.firstName());
        assertThat(expectedAuthor.lastName())
                .isNull();
        assertThat(actualAuthor.idBook())
                .isEqualTo(expectedAuthor.idBook());
    }

    @Test
    void shouldCreateEmptyAuthorWithoutValues() {
        AuthorRequest expectedAuthor =
                AuthorTestData.withoutValues();

        Response response =
                authorsSteps.createAuthor(expectedAuthor);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Author actualAuthor =
                response.as(Author.class);

        assertThat(actualAuthor.id())
                .isEqualTo(0);
        assertThat(actualAuthor.firstName())
                .isNull();;
        assertThat(expectedAuthor.lastName())
                .isNull();
        assertThat(actualAuthor.idBook())
                .isEqualTo(0);
    }

    @Test
    void shouldUpdateAuthor() {
        Response response =
                authorsSteps.updateAuthor(
                        AuthorTestData.EXISTING_AUTHOR_ID,
                        AuthorTestData.basicPut()
                );

        assertThat(response.statusCode())
                .isEqualTo(200);
    }


    
   @Test
    void shouldRejectUpdateWhenAuthorIdIsEmpty() {
        Response response =
                authorsSteps.updateAuthorWithoutId(
                        AuthorTestData.basicPost()
                );

        assertThat(response.statusCode())
                .isEqualTo(405);
    }


    @Test
    void shouldRejectUpdateWhenAuthorIdIsNull() {
        Response response =
                authorsSteps.updateAuthorWithIdNull(
                        AuthorTestData.basicPut()
                );

        assertThat(response.statusCode())
                .isEqualTo(400);
    }

    @Test
    void shouldDeleteAuthor() {
        Response response =
                authorsSteps.deleteAuthor(
                        AuthorTestData.EXISTING_AUTHOR_ID
                );

        assertThat(response.statusCode())
                .isEqualTo(200);
    }

    @Test
    void shouldNotDeleteAuthorWithInvalidId() {
        Response response =
                authorsSteps.deleteAuthorWithInvalidId("someId");

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("errors.id[0]"))
            .isEqualTo("The value 'someId' is not valid.");

    }
}