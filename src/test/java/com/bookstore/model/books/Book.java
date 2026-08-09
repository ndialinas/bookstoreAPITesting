package com.bookstore.model.books;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder(toBuilder = true)
public record Book(
        Integer id,
        String title,
        String description,
        Integer pageCount,
        String excerpt,
        String publishDate
) {
}