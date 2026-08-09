package com.bookstore.model.authors;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record AuthorRequest(
        Integer id,
        Integer idBook,
        String  firstName,
        String  lastName
) {


    public Author toAuthor(Integer id) {
        return new Author(
                id,
                idBook,
                firstName,
                lastName
        );
    }
}