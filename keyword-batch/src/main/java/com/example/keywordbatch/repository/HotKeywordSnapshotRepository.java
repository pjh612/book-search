package com.example.keywordbatch.repository;

import com.example.keywordbatch.entity.HotKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface HotKeywordSnapshotRepository extends JpaRepository<HotKeyword, Long> {
    @Modifying
    @Query("update HotKeyword k " +
            "set k.count = :count, k.snapshotTime = :snapshotTime " +
            "where k.keyword = :keyword")
    int updateCount(@Param("keyword") String keyword,
                    @Param("count") Long count,
                    @Param("snapshotTime") LocalDateTime snapshotTime);
}
