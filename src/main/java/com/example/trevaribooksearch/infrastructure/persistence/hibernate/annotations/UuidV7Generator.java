package com.example.trevaribooksearch.infrastructure.persistence.hibernate.annotations;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@IdGeneratorType(com.example.trevaribooksearch.infrastructure.persistence.hibernate.UuidV7Generator.class)
public @interface UuidV7Generator {
}
