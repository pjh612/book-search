package com.example.trevaribooksearch.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorTest {

    @Test
    @DisplayName("Author 생성자는 이름이 없으면 예외를 던진다")
    void constructor_validation() {
        assertThatThrownBy(() -> new Author(UUID.randomUUID(), null, null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Author(UUID.randomUUID(), "", null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new Author(UUID.randomUUID(), "   ", null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Author 생성자는 정상적으로 Author를 생성한다")
    void constructor_success() {
        UUID id = UUID.randomUUID();
        Author author = new Author(id, "홍길동", null);

        assertThat(author.getId()).isEqualTo(id);
        assertThat(author.getName()).isEqualTo("홍길동");
        assertThat(author.getAuditInfo()).isNull();
    }

    @Test
    @DisplayName("rename은 이름을 정상적으로 변경한다")
    void rename_success() {
        Author author = new Author(UUID.randomUUID(), "홍길동", null);
        author.rename("이몽룡");
        assertThat(author.getName()).isEqualTo("이몽룡");
    }

    @Test
    @DisplayName("rename은 잘못된 이름 입력 시 예외를 던진다")
    void rename_validation() {
        Author author = new Author(UUID.randomUUID(), "홍길동", null);
        assertThatThrownBy(() -> author.rename(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> author.rename(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> author.rename("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }
}