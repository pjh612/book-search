package com.example.bookapi.infrastructure.openapi.annotation;


public @interface ExceptionCodeExample {

    String title();

    String description() default "";

    String code();
}

