package com.example.bookapi.infrastructure.openapi;

import com.example.bookapi.common.exception.ErrorResponse;
import com.example.bookapi.common.exception.ApplicationException;
import com.example.bookapi.common.exception.converter.ExceptionCodeConverter;
import com.example.bookapi.infrastructure.openapi.annotation.ApiErrorCodeExample;
import com.example.bookapi.infrastructure.openapi.annotation.ExceptionCodeExample;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.web.method.HandlerMethod;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class ErrorResponseOperationCustomizer implements OperationCustomizer {

    private final ExceptionCodeConverter exceptionCodeConverter;

    public ErrorResponseOperationCustomizer(ExceptionCodeConverter exceptionCodeConverter) {
        this.exceptionCodeConverter = exceptionCodeConverter;
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        ApiErrorCodeExample apiErrorCodeExamples = handlerMethod.getMethodAnnotation(ApiErrorCodeExample.class);
        if (apiErrorCodeExamples == null) {
            return operation;
        }
        ExceptionCodeExample[] examples = apiErrorCodeExamples.examples();

        List<String> tags = getTags(handlerMethod);
        if (!tags.isEmpty()) {
            operation.setTags(Collections.singletonList(tags.get(0)));
        }
        if (examples != null) {
            generateErrorCodeResponseExample(operation, examples);
        }

        return operation;
    }

    private void generateErrorCodeResponseExample(
            Operation operation, ExceptionCodeExample[] examples) {
        ApiResponses responses = operation.getResponses();

        Map<Integer, List<ExampleHolder>> statusWithExampleHolders = Arrays.stream(examples)
                .map(example -> {
                    ApplicationException exception = exceptionCodeConverter.toError(example.code());
                    ErrorResponse errorResponse = exceptionCodeConverter.toResponse(exception);
                    Example swaggerExample = new Example()
                            .description(example.description())
                            .value(errorResponse);

                    Integer status = exception.getExceptionContent().getStatus();
                    return new ExampleHolder(swaggerExample, example.title(), status);
                })
                .collect(Collectors.groupingBy(ExampleHolder::getCode));

        addExamplesToResponses(responses, statusWithExampleHolders);
    }

    private void addExamplesToResponses(ApiResponses responses, Map<Integer, List<ExampleHolder>> statusWithExampleHolders) {
        statusWithExampleHolders.forEach((status, v) -> {
            Content content = new Content();
            MediaType mediaType = new MediaType();
            ApiResponse apiResponse = new ApiResponse();
            v.forEach(exampleHolder -> mediaType.addExamples(exampleHolder.getName(), exampleHolder.getHolder()));
            content.addMediaType("application/json", mediaType);
            apiResponse.setContent(content);
            responses.addApiResponse(status.toString(), apiResponse);
        });
    }

    private static List<String> getTags(HandlerMethod handlerMethod) {
        List<String> tags = new ArrayList<>();

        Tag[] methodTags = handlerMethod.getMethod().getAnnotationsByType(Tag.class);
        List<String> methodTagStrings =
                Arrays.stream(methodTags)
                        .map(Tag::name)
                        .toList();

        Tag[] classTags = handlerMethod.getClass().getAnnotationsByType(Tag.class);
        List<String> classTagStrings =
                Arrays.stream(classTags)
                        .map(Tag::name)
                        .toList();
        tags.addAll(methodTagStrings);
        tags.addAll(classTagStrings);

        return tags;
    }
}