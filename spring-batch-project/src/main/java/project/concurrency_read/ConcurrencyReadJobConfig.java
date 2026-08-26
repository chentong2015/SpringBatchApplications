package project.concurrency_read;

import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import project.bean.DbRecord;
import project.bean.Record;

@Configuration
public class ConcurrencyReadJobConfig {

    // TODO. 多线程并发执行myWorkerStep, 提供Step执行上下文, 并发读取同一个大文件
    @Bean
    public Step myWorkerStep(JobRepository jobRepository, RandomAccessFileItemReader randomAccessFileItemReader) {
        return new StepBuilder("My Worker Step", jobRepository)
                .<Record, DbRecord>chunk(1000)
                .reader(randomAccessFileItemReader)
                // .processor(processor())
                // .writer(writer())
                .build();
    }
}
