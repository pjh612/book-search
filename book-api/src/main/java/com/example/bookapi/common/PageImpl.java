package com.example.bookapi.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true, value = {"pageable"})
public class PageImpl<T> extends org.springframework.data.domain.PageImpl<T> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PageImpl(@JsonProperty("content") List<T> content,
                    @JsonProperty("number") int page,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") long total) {
        super(content, PageRequest.of(page, size), total);
    }

    public PageImpl(Page<T> page) {
        super(page.getContent(), page.getPageable(), page.getTotalElements());
    }

    public PageImpl(List<T> content, Pageable pageable, long total) {
        super(content, pageable, total);
    }
}
