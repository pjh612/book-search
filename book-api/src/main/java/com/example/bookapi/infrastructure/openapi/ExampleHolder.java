package com.example.bookapi.infrastructure.openapi;

import io.swagger.v3.oas.models.examples.Example;

public class ExampleHolder {
    private Example holder;
    private String name;
    private Integer code;

    public Example getHolder() {
        return holder;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

    public ExampleHolder(Example holder, String name, Integer code) {
        this.holder = holder;
        this.name = name;
        this.code = code;
    }
}
