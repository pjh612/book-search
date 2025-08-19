package com.example.bookweb.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class PageImpl<T> extends org.springframework.data.domain.PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageImpl(@JsonProperty("content") List<T> content, @JsonProperty("number") int number,
                    @JsonProperty("size") int size, @JsonProperty("totalElements") Long totalElements,
                    @JsonProperty("pageable") JsonNode pageable, @JsonProperty("last") boolean last,
                    @JsonProperty("totalPages") int totalPages, @JsonProperty("sort") JsonNode sort,
                    @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, 1), 10);
    }

    public PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageImpl(List<T> content) {
        super(content);
    }

    public PageImpl() {
        super(new ArrayList<>());
    }
}
