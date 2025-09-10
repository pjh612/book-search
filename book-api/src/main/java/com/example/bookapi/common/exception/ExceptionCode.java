package com.example.bookapi.common.exception;


import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ExceptionCode implements ExceptionContent {
    NOT_FOUND_BOOK("NOT_FOUND_BOOK", "도서 정보를 찾을 수 없습니다.", 404),
    AUTHENTICATION_FAILED("AUTHENTICATION_FAILED", "아이디 비밀번호를 확인해주세요.", 401),
    ID_DUPLICATED("ID_DUPLICATED", "중복된 아이디의 사용자가 존재합니다.", 400),
    INVALID_SEARCH_QUERY("INVALID_SEARCH_QUERY", "올바른 검색 입력이 아닙니다.", 400),
    INVALID_TOKEN("INVALID_TOKEN", "유효하지 않은 토큰입니다.", 401),
    NOT_FOUND_AUTHOR("NOT_FOUND_AUTHOR", "존재하지 않는 저자 입니다.", 400),
    NOT_FOUND_PUBLISHER("NOT_FOUND_PUBLISHER", "존재하지 않는 출판사 입니다.", 400),
    SEARCH_FAILED("SEARCH_FAILED", "검색 시스템 오류가 발생했습니다.", 500);

    private final String code;
    private final String detail;
    private final int status;

    private static final Map<String, ExceptionCode> codeMap = Arrays.stream(ExceptionCode.values()).collect(Collectors.toMap(ExceptionCode::getCode, exceptionCode -> exceptionCode));

    ExceptionCode(String code, String detail, Integer status) {
        this.code = code;
        this.detail = detail;
        this.status = status;

    }

    @Override
    public String getTitle() {
        return this.name();
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    public Integer getStatus() {
        return status;
    }

    public String toString() {
        return name() + "-" + code + ":" + detail;
    }

    public static Optional<ExceptionCode> lookupByCode(String code) {
        return Optional.ofNullable(codeMap.get(code));
    }
}
