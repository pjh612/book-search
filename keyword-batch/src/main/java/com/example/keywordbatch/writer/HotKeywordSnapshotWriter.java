package com.example.keywordbatch.writer;

import com.example.keywordbatch.entity.HotKeyword;
import com.example.keywordbatch.repository.HotKeywordSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@StepScope
@Component
@RequiredArgsConstructor
public class HotKeywordSnapshotWriter implements ItemWriter<HotKeyword> {
    private final HotKeywordSnapshotRepository repository;

    @Override
    public void write(Chunk<? extends HotKeyword> items) throws Exception {
        for (HotKeyword item : items) {
            int updated = repository.updateCount(item.getKeyword(), item.getCount(), item.getSnapshotTime());
            if (updated == 0) repository.save(item);
        }
    }

}
