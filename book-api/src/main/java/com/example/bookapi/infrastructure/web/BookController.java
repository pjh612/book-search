package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.BookCursor;
import com.example.bookapi.application.dto.BookDetailResponse;
import com.example.bookapi.application.dto.BookResponse;
import com.example.bookapi.application.dto.BookSearchRequest;
import com.example.bookapi.application.dto.BookSearchResponse;
import com.example.bookapi.application.facade.QueryBookFacade;
import com.example.bookapi.application.in.QueryBookUseCase;
import com.example.bookapi.common.model.CursorPageResponse;
import com.example.bookapi.infrastructure.openapi.annotation.ApiErrorCodeExample;
import com.example.bookapi.infrastructure.openapi.annotation.ExceptionCodeExample;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Books", description = "도서 조회 API")
@SecurityRequirement(name = "bearerAuth")
public class BookController {

    private final QueryBookUseCase queryBookUseCase;
    private final QueryBookFacade queryBookFacade;

    @GetMapping

    @Operation(
            summary = "도서 목록 조회 (페이지네이션)",
            description = "page/size/sort 파라미터로 도서 목록을 조회합니다. 기본 정렬은 createdAt desc 입니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공")
    })
    Page<BookResponse> getBooks(@ParameterObject @PageableDefault Pageable pageable) {
        return queryBookUseCase.findBooks(pageable);
    }

    @GetMapping("/cursor")
    @Operation(
            summary = "도서 목록 조회 (커서 기반)",
            description = "커서(마지막 항목의 생성시각)를 기준으로 다음 페이지를 조회합니다. 최신순(createdAt desc)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공")
    })
    CursorPageResponse<BookCursor, BookDetailResponse> getBooks(@Valid @ModelAttribute BookCursor cursor,
                                                                @RequestParam(defaultValue = "10") int size) {
        return queryBookUseCase.findBooks(cursor, size);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "도서 단건 조회",
            description = "도서 ID(UUID)로 상세 정보를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @ApiErrorCodeExample(examples = {
            @ExceptionCodeExample(title = "도서 정보가 존재하지 않을 때", code = "NOT_FOUND_BOOK")
    })
    BookDetailResponse getById(
            @Parameter(description = "도서 ID(UUID)")
            @PathVariable UUID id
    ) {
        return queryBookUseCase.findById(id);
    }

    @GetMapping("/search")
    @Operation(
            summary = "도서 검색",
            description = "키워드(제목 기준)로 도서를 검색합니다. OR: |, NOT: - 연산자를 지원합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    @ApiErrorCodeExample(examples = {
            @ExceptionCodeExample(title = "검색 키워드가 유효하지 않을 때", code = "INVALID_SEARCH_QUERY")
    })
    BookSearchResponse searchBooks(
            @Parameter(description = "검색 키워드", example = "자바|스프링")
            @RequestParam @Valid @NotBlank @Size(min = 1, max = 100) String keyword,
            @ParameterObject @PageableDefault Pageable pageable
    ) {
        return queryBookFacade.searchBooks(new BookSearchRequest(keyword, pageable));
    }
}