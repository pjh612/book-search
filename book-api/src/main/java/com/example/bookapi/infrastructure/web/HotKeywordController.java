package com.example.bookapi.infrastructure.web;

import com.example.bookapi.application.dto.HotKeywordResponse;
import com.example.bookapi.application.in.QueryHotKeywordUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hot-keywords")
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "HotKeyword", description = "인기 검색어 조회 API")
public class HotKeywordController {
    private final QueryHotKeywordUseCase queryHotKeywordUseCase;

    @GetMapping
    @Operation(
            summary = "인기 검색어 조회",
            description = "특정 날짜 기준 인기 검색어 상위 N개를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
    })
    public HotKeywordResponse getHotKeyword(
            @Parameter(description = "조회할 키워드 개수(상위 N)", example = "5")
            @RequestParam int size,
            @Parameter(description = "기준 날짜(yyyy-MM-dd)",
                    schema = @Schema(type = "string", format = "date", example = "2025-08-21"))
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @RequestParam LocalDate date
    ) {
        return queryHotKeywordUseCase.find(size, date);
    }
}
