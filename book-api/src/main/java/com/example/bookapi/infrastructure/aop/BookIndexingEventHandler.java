package com.example.bookapi.infrastructure.aop;

import com.example.bookapi.application.event.CreatedBookEvent;
import com.example.bookapi.domain.model.Author;
import com.example.bookapi.domain.model.Book;
import com.example.bookapi.domain.model.Publisher;
import com.example.bookapi.infrastructure.search.elasticsearch.document.BookDocument;
import com.example.bookapi.infrastructure.search.elasticsearch.repository.BookDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookIndexingEventHandler {
    private final BookDocumentRepository repository;

    @TransactionalEventListener
    public void indexBookAfterSave(CreatedBookEvent event) {
        Author author = event.author();
        Book book = event.book();
        Publisher publisher = event.publisher();
        BookDocument bookDocument = new BookDocument(
                book.getId(),
                book.getIsbn(),
                book.getTitle(),
                book.getSubtitle(),
                book.getImage(),
                book.getPublished(),
                publisher.getId(),
                publisher.getName(),
                author.getId(),
                author.getName(),
                book.getAuditInfo().getCreatedBy(),
                book.getAuditInfo().getUpdatedBy(),
                book.getAuditInfo().getCreatedAt(),
                book.getAuditInfo().getUpdatedAt()
        );

        repository.save(bookDocument);

        log.info("Indexed book: {}", bookDocument.getId());
    }
}
