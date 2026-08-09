package com.bookstore.data.authors;

import com.bookstore.model.authors.AuthorRequest;

public final class AuthorTestData {

    public static final Integer EXISTING_AUTHOR_ID = 1;

    private AuthorTestData() {
    }

    public static AuthorRequest basicPost() {
        return AuthorRequest.builder().firstName("TestAuthor firstName").lastName("TestAuthor lastName").build();
    }

    public static AuthorRequest basicPut() {
        return AuthorRequest.builder().firstName("Updated firstName").lastName("Update LastName").build();
    }

    public static AuthorRequest withBookId() {
        return AuthorRequest.builder().firstName("TestAuthor firstName").lastName("TestAuthor lastName").idBook(1).build();
    }

    public static AuthorRequest withId() {
        return AuthorRequest.builder().id(3).firstName("TestAuthor firstName").lastName("TestAuthor lastName").build();
    }

    public static AuthorRequest withoutLastName() {
        return AuthorRequest.builder().firstName("TestAuthor firstName").idBook(1).build();
    }

    public static AuthorRequest withoutValues() {
        return AuthorRequest.builder().build();
    }

    
}