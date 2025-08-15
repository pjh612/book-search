package com.example.trevaribooksearch.domain.model;

import lombok.Getter;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
public final class Isbn {
    private static final Pattern ISBN13 = Pattern.compile("^(97[89])\\d{10}$");
    private static final Pattern ISBN10 = Pattern.compile("^\\d{9}[\\dX]$");

    private final String value;

    private Isbn(String value) {
        this.value = value;
    }

    public static Isbn of(String raw) {
        String v = raw == null ? null : raw.replaceAll("[^0-9Xx]", "").toUpperCase();
        if (v == null || !(ISBN13.matcher(v).matches() || ISBN10.matcher(v).matches())) {
            throw new IllegalArgumentException("Invalid ISBN: " + raw);
        }
        return new Isbn(v);
    }

    public String value() { return value; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Isbn)) return false;
        Isbn isbn = (Isbn) o;
        return Objects.equals(value, isbn.value);
    }

    @Override public int hashCode() { return Objects.hash(value); }

    @Override public String toString() { return value; }

    public static String randomIsbn13() {
        // 978 또는 979 접두사 선택
        String prefix = Math.random() < 0.5 ? "978" : "979";
        // 9자리 랜덤 숫자 생성
        StringBuilder sb = new StringBuilder(prefix);
        for (int i = 0; i < 9; i++) {
            sb.append((int) (Math.random() * 10));
        }
        // 체크디지트 계산
        String first12 = sb.toString();
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = first12.charAt(i) - '0';
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int check = (10 - (sum % 10)) % 10;
        sb.append(check);
        return sb.toString();
    }
}
