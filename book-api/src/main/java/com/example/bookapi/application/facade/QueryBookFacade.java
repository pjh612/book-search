package com.example.bookapi.application.facade;

import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.event.SearchEvent;
import com.example.bookapi.application.in.QueryBookUseCase;
import com.example.bookapi.application.out.MessagePublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueryBookFacade {
    private final QueryBookUseCase queryBookUseCase;
    private final MessagePublisher messagePublisher;

    public BookSearchResponse searchBooks(BookSearchRequest request) {
        BookSearchResponse response = queryBookUseCase.searchBooks(request);
        messagePublisher.publish("search-keyword", new SearchEvent(request.keyword().trim()));

        return response;
    }
}
