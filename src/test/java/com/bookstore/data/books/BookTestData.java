package com.bookstore.data.books;

import com.bookstore.model.books.BookRequest;

public final class BookTestData {

    public static final Integer EXISTING_BOOK_ID = 1;

    private BookTestData() {
    }

    public static BookRequest basicPost() {
        return BookRequest.builder().title("Serenity API Test Book").description("Book used by the POST test").pageCount(250).excerpt("Test excerpt").publishDate("2026-08-08T00:00:00Z").build();
    }

    public static BookRequest basicPut() {
        return BookRequest.builder().title("Updated Book Title").description("Updated book description").pageCount(300).excerpt("Updated excerpt").publishDate("2026-08-08T00:00:00Z").build();
    }

    public static BookRequest noPublishDate() {
        return BookRequest.builder().title("Serenity API Test Book").description("Book used by the POST test").pageCount(250).excerpt("Test excerpt").build();
    }

    public static BookRequest wrongPublishDate() {
        return BookRequest.builder().title("Serenity API Test Book").description("Book used by the POST test").pageCount(250).excerpt("Test excerpt").publishDate("WrongDate").build();
    }

    public static BookRequest noPageCount() {
        return BookRequest.builder().title("Serenity API Test Book").description("Book used by the POST test").excerpt("Test excerpt").publishDate("2026-08-08T00:00:00Z").build();
    }
}