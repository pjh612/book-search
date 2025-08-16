package com.example.bookapi;

import com.example.bookapi.domain.model.Author;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.model.Isbn;
import com.example.bookapi.domain.model.Publisher;
import com.example.bookapi.domain.repository.AuthorRepository;
import com.example.bookapi.domain.repository.BookRepository;
import com.example.bookapi.domain.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@Profile(("!test"))
public class DummyDataInitializer {
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;
    private final BookRepository bookRepository;

    @Bean
    public ApplicationRunner initDummyData() {
        return args -> {
            Author author = authorRepository.save(new Author(null, "테스트 저자", null));
            Publisher publisher = publisherRepository.save(new Publisher(null, "테스트 출판사", null));

            List<Book> books = new ArrayList<>();
            for (int i = 1; i <= 120; i++) {
                books.add(Book.create(
                        "테스트 책 제목 " + i,
                        "테스트 부제목 " + i,
                        null,
                        Isbn.randomIsbn13(),
                        author.getId(),
                        publisher.getId(),
                        Instant.now().minus(i, ChronoUnit.DAYS),
                        "system"
                ));
            }
            bookRepository.saveAll(books);
        };
    }
}
