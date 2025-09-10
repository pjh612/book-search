package com.example.bookapi.application.service;

import com.example.bookapi.application.dto.CreateBookRequest;
import com.example.bookapi.application.event.CreatedBookEvent;
import com.example.bookapi.application.in.CreateBookUseCase;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.common.exception.ExceptionCode;
import com.example.bookapi.domain.model.Author;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.model.Publisher;
import com.example.bookapi.domain.repository.AuthorRepository;
import com.example.bookapi.domain.repository.BookRepository;
import com.example.bookapi.domain.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateBookService implements CreateBookUseCase {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Transactional
    public UUID create(CreateBookRequest request) {
        Author author = authorRepository.findById(request.authorId())
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND_AUTHOR));
        Publisher publisher = publisherRepository.findById(request.publisherId())
                .orElseThrow(() -> new ApplicationException(ExceptionCode.NOT_FOUND_PUBLISHER));
        Book book = Book.create(request.title(),
                request.subtitle(),
                request.image(),
                request.isbn(),
                request.authorId(),
                request.publisherId(),
                request.published(),
                null
        );

        Book savedBook = bookRepository.save(book);

        eventPublisher.publishEvent(new CreatedBookEvent(
                book,
                author,
                publisher
        ));

        return savedBook.getId();
    }
}
