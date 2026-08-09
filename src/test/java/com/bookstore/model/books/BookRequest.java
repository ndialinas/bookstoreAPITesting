package com.bookstore.model.books;

import lombok.Builder;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record BookRequest(
        Integer id,
        String  title,
        String  description,
        Integer pageCount,
        String  excerpt,
        String  publishDate
) {

  public Book toBook(Integer id) {
        return new Book(
                id,
                title,
                description,
                pageCount,
                excerpt,
                publishDate
        );
    }
}