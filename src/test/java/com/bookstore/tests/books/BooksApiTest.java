package com.bookstore.tests.books;


import com.bookstore.data.books.BookTestData;
import com.bookstore.model.books.Book;
import com.bookstore.model.books.BookRequest;
import com.bookstore.tests.ApiTestBase;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

class BooksApiTest extends ApiTestBase {

    @Test
    void shouldRetrieveAllBooks() {
        var books = booksSteps.getAllBooks();

        assertThat(books)
                .isNotNull()
                .isNotEmpty();

        assertThat(books)
                .allSatisfy(book -> {
                    assertThat(book.id()).isNotNull();
                    assertThat(book.title()).isNotBlank();
                });
    }

    @Test
    void shouldRetrieveBookById() {
        Response response =
                booksSteps.getBookById(
                        BookTestData.EXISTING_BOOK_ID
                );

        assertThat(response.statusCode())
                .isEqualTo(200);

        Book actualBook =
                response.as(Book.class);

        assertThat(actualBook.id())
                .isEqualTo(BookTestData.EXISTING_BOOK_ID);
        assertThat(actualBook.title())
                .isEqualTo("Book 1");
    }

    @Test
    void shouldNotRetrieveBookWhenIdIsNull() {
        Response response =
                booksSteps.getBookByNullId();

        assertThat(response.statusCode())
                .isEqualTo(400);

    }

    @Test
    void shouldNotRetrieveBookWhenIdIsString() {
        Response response =
                booksSteps.getBookByInvalidId("someId");

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("errors.id[0]"))
            .isEqualTo("The value 'someId' is not valid.");

    }

    @Test
    void shouldNotCreateBookWithMalformedDate() {
        BookRequest request =
                BookTestData.wrongPublishDate();

        Response response =
                booksSteps.createBook(request);

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getList("errors.'$.publishDate'", String.class).get(0))
            .contains("The JSON value could not be converted to System.DateTime. Path: $.publishDate");
    }

    @Test
    void shouldCreateBook() {
        BookRequest request =
                BookTestData.basicPost();

        Response response =
                booksSteps.createBook(request);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Book actualBook =
                response.as(Book.class);

        Book expectedBook =
            request.toBook(actualBook.id());

        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

   
    @Test
    void shouldRejectCreatingBookWithInvalidIdValues() {
        Map<String, String> invalidBook = Map.of(
        "id", "3333333333333333333333333333333333333333333",
        "title", "string",
        "description", "string",
        "pageCount", "0",
        "excerpt", "string",
        "publishDate", "2026-08-09T19:57:00.793Z"
        );
        
        Response response =
                booksSteps.createBookInvalidValues(invalidBook);

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getList("errors.'$.id'", String.class).get(0))
            .contains("The JSON value could not be converted to System.Int32. Path: $.id");
    }

    @Test
    void shouldRejectCreatingBookWithInvalidPageCountValues() {
       Map<String, String> invalidBook = Map.of(
        "id", "3",
        "title", "0",
        "description", "string",
        "pageCount", "string",
        "excerpt", "string",
        "publishDate", "2026-08-09T19:57:00.793Z"
        );
        
        Response response =
                booksSteps.createBookInvalidValues(invalidBook);

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getList("errors.'$.pageCount'", String.class).get(0))
            .contains("The JSON value could not be converted to System.Int32. Path: $.pageCount");
    }

    @Test
    void shouldCreateBookWithNoDate() {
        BookRequest request =
                BookTestData.noPublishDate();

        Response response =
                booksSteps.createBook(request);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Book actualBook =
                response.as(Book.class);

        Book expectedBook =
            request.toBook(actualBook.id()).toBuilder()
                .publishDate("0001-01-01T00:00:00")
                .build();
        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    @Test
    void shouldCreateEmptyBookWithoutValues() {
        BookRequest expectedBook =
               BookTestData.withoutValues();

        Response response =
                booksSteps.createBook(expectedBook);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Book actualBook =
                response.as(Book.class);

        assertThat(actualBook.id())
                .isEqualTo(0);
        assertThat(actualBook.title())
                .isNull();;
        assertThat(actualBook.description())
                .isNull();
         assertThat(actualBook.excerpt())
                .isNull();
        assertThat(actualBook.pageCount())
                .isEqualTo(0);
        assertThat(actualBook.publishDate())
                .isEqualTo("0001-01-01T00:00:00");
    }

    @Test
    void shouldCreateBookWithNoPageCount() {
        BookRequest request =
                BookTestData.noPageCount();

        Response response =
                booksSteps.createBook(request);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Book actualBook =
                response.as(Book.class);

        Book expectedBook =
            request.toBook(actualBook.id()).toBuilder()
                .pageCount(0)
                .build();;
        assertThat(actualBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
    }

    @Test
    void shouldUpdateBook() {
        BookRequest request = BookTestData.basicPut();
        Response response =
                booksSteps.updateBook(
                        BookTestData.EXISTING_BOOK_ID,
                        request
                );

        assertThat(response.statusCode())
                .isEqualTo(200);
        Book updatedBook =
                response.as(Book.class);
        Book expectedBook =
            request.toBook(updatedBook.id());

        assertThat(updatedBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
        
      }

    @Test
    void shouldUpdateBookWithNoPublishDate() {
        BookRequest request = BookTestData.noPublishDate();
        Response response =
                booksSteps.updateBook(
                        BookTestData.EXISTING_BOOK_ID,
                        request
                );

        assertThat(response.statusCode())
                .isEqualTo(200);
        Book updatedBook =
                response.as(Book.class);
        Book expectedBook =
            request.toBook(updatedBook.id()).toBuilder()
                .publishDate("0001-01-01T00:00:00")
                .build();;

        assertThat(updatedBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
        
      }

    
    @Test
    void shouldUpdateBookWithNoPageCount() {
        BookRequest request = BookTestData.noPageCount();
        Response response =
                booksSteps.updateBook(
                        BookTestData.EXISTING_BOOK_ID,
                        request
                );

        assertThat(response.statusCode())
                .isEqualTo(200);
        

         Book updatedBook =
                response.as(Book.class);
        Book expectedBook =
            request.toBook(updatedBook.id()).toBuilder()
                .pageCount(0)
                .build();;

        assertThat(updatedBook)
            .usingRecursiveComparison()
            .isEqualTo(expectedBook);
        
    }

    @Test
    void shouldUpdateBookWithoutValues() {
        BookRequest expectedBook =
               BookTestData.withoutValues();

        Response response =
                booksSteps.updateBook(BookTestData.EXISTING_BOOK_ID,expectedBook);

        assertThat(response.statusCode())
                .isEqualTo(200);

        Book actualBook =
                response.as(Book.class);

        assertThat(actualBook.id())
                .isEqualTo(0);
        assertThat(actualBook.title())
                .isNull();;
        assertThat(actualBook.description())
                .isNull();
         assertThat(actualBook.excerpt())
                .isNull();
        assertThat(actualBook.pageCount())
                .isEqualTo(0);
        assertThat(actualBook.publishDate())
                .isEqualTo("0001-01-01T00:00:00");
    }

    @Test
    void shouldNotUpdateBookWithWrongPublishDate() {
        BookRequest request = BookTestData.wrongPublishDate();
        Response response =
                booksSteps.updateBook(
                        BookTestData.EXISTING_BOOK_ID,
                        request
                );

        assertThat(response.statusCode())
                .isEqualTo(400);
        

        assertThat(response.jsonPath().getList("errors.'$.publishDate'", String.class).get(0))
            .contains("The JSON value could not be converted to System.DateTime. Path: $.publishDate");
        
    }

    @Test
    void shouldNotUpdateBookWithInvalidId() {
        BookRequest request = BookTestData.basicPut();
        Response response =
                booksSteps.updateBookInvalidId(
                        "someId",
                        request
                );

        assertThat(response.statusCode())
                .isEqualTo(400);
        

        assertThat(response.jsonPath().getString("errors.id[0]"))
            .isEqualTo("The value 'someId' is not valid.");
        
    }

    @Test
    void shouldRejectUpdateWhenBookIdIsEmpty() {
        Response response =
                booksSteps.updateBookWithoutId(
                        BookTestData.basicPost()
                );

        assertThat(response.statusCode())
                .isEqualTo(405);
    }

      

    @Test
    void shouldDeleteBook() {
        Response response =
                booksSteps.deleteBook(BookTestData.EXISTING_BOOK_ID);

        assertThat(response.statusCode())
                .isEqualTo(200);

    }

    @Test
    void shouldNotDeleteBookWithInvalidId() {
        Response response =
                booksSteps.deleteBookInvalidId("someId");

        assertThat(response.statusCode())
                .isEqualTo(400);

        assertThat(response.jsonPath().getString("errors.id[0]"))
            .isEqualTo("The value 'someId' is not valid.");

    }
}