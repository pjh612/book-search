package com.example.keywordbatch.job;

import com.example.keywordbatch.reader.HotKeywordRedisReader;
import com.example.keywordbatch.writer.HotKeywordSnapshotWriter;
import com.example.keywordbatch.entity.HotKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class HotKeywordSnapshotJobConfig {
    private final JobRepository jobRepository;
    private final PlatformTransactionManager txManager;
    private final HotKeywordRedisReader reader;
    private final HotKeywordSnapshotWriter writer;

    @Bean
    public Job hotKeywordSnapshotJob() {
        return new JobBuilder("hotKeywordSnapshotJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(hotKeywordSnapshotStep())
                .build();
    }

    @Bean
    public Step hotKeywordSnapshotStep() {
        return new StepBuilder("hotKeywordBackupStep", jobRepository)
                .<HotKeyword, HotKeyword>chunk(100, txManager)
                .reader(reader)
                .writer(writer)
                .build();
    }
}