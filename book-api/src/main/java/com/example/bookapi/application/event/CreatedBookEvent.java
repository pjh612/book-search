package com.example.bookapi.application.event;

import com.example.bookapi.domain.model.Author;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.model.Publisher;

public record CreatedBookEvent(
        Book book,
        Author author,
        Publisher publisher
) {
}
