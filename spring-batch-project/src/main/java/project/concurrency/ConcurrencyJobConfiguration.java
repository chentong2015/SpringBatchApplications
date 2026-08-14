package project.concurrency;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcurrencyJobConfiguration {

    @Bean(name = "concurrencyJob")
    public Job concurrencyJob(JobRepository jobRepository, @Qualifier("concurrencyStep") Step concurrencyStep) {
        return new JobBuilder("Concurrency Job", jobRepository)
                .preventRestart()
                .incrementer(new RunIdIncrementer())
                .start(concurrencyStep)
                .build();
    }

    // TODO. .tasklet()并发执行效果
    @Bean(name = "concurrencyStep")
    public Step concurrencyStep(JobRepository jobRepository, ParallelChunkTasklet chunkParallelTasklet) {
        return new StepBuilder("Concurrency Step", jobRepository)
                .tasklet(chunkParallelTasklet)
                .build();
    }

    // TODO. .taskExecutor()设置无法让chunk操作并行
    // @Bean(name = "concurrencyStep")
    // public Step concurrencyStep(JobRepository jobRepository,
    //                             RecordItemReader itemReader,
    //                             RecordItemProcessor itemProcessor,
    //                             RecordItemWriter itemWriter,
    //                             ThreadPoolTaskExecutor taskExecutor) {
    //     return new StepBuilder("Concurrency Step", jobRepository)
    //             .<Record, DbRecord>chunk(100)
    //             .reader(new SynchronizedItemStreamReader<>(itemReader))
    //             .processor(itemProcessor)
    //             .writer(itemWriter)
    //             .taskExecutor(taskExecutor)
    //             .build();
    // }
}