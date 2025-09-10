package com.example.bookapi.infrastructure.search.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.UUID;

@Getter
@Document(indexName = "book")
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookDocument {
    @Field(type = FieldType.Keyword)  // Text → Keyword
    private UUID id;

    @Field(type = FieldType.Keyword)  // Text → Keyword
    private String isbn;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String subtitle;

    @Field(type = FieldType.Keyword)  // Text → Keyword
    private String image;

    @Field(type = FieldType.Date)     // Text → Date
    private Instant published;

    @Field(type = FieldType.Keyword)  // Text → Keyword
    private UUID publisherId;

    @Field(type = FieldType.Text)
    private String publisher;

    @Field(type = FieldType.Keyword)  // Text → Keyword
    private UUID authorId;

    @Field(type = FieldType.Text)
    private String author;

    @Field(type = FieldType.Keyword)  // Text → Keyword
    private String createdBy;

    @Field(type = FieldType.Keyword)  // Text → Keyword
    private String updatedBy;

    @Field(type = FieldType.Date)
    private Instant createdAt;

    @Field(type = FieldType.Date)
    private Instant updatedAt;
}
