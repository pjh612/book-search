package com.example.keywordbatch.job;

import com.example.keywordbatch.entity.HotKeyword;
import com.example.keywordbatch.reader.HotKeywordRedisReader;
import com.example.keywordbatch.repository.HotKeywordSnapshotRepository;
import com.example.keywordbatch.writer.HotKeywordSnapshotWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@SpringBatchTest
@ActiveProfiles("test")
@Import({HotKeywordSnapshotJobConfig.class, HotKeywordRedisReader.class, HotKeywordSnapshotWriter.class})
class HotKeywordSnapshotJobIntegrationTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private HotKeywordSnapshotRepository repository;

    private final String testDate = "2025-08-17";

    @BeforeEach
    void setupRedis() {
        redisTemplate.delete("search:hot-keyword:day:" + testDate);
        redisTemplate.opsForZSet().add("search:hot-keyword:day:" + testDate, "spring", 5);
        redisTemplate.opsForZSet().add("search:hot-keyword:day:" + testDate, "java", 3);
    }

    @Test
    void hotKeywordSnapshotJob_executesAndPersists() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("date", testDate)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        JobExecution execution = jobLauncherTestUtils.launchJob(jobParameters);

        assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        HotKeyword spring = repository.findAll()
                .stream()
                .filter(k -> k.getKeyword().equals("spring"))
                .findFirst().orElse(null);

        HotKeyword java = repository.findAll()
                .stream()
                .filter(k -> k.getKeyword().equals("java"))
                .findFirst().orElse(null);

        assertThat(spring).isNotNull();
        assertThat(spring.getCount()).isEqualTo(5L);

        assertThat(java).isNotNull();
        assertThat(java.getCount()).isEqualTo(3L);
    }
}
