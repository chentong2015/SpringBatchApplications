package project.concurrency;

import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import project.base_bean.DbRecord;
import project.base_bean.Record;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import project.base_process.RecordItemProcessor;
import project.base_process.RecordItemReaderWrapper;
import project.base_process.RecordItemWriter;

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

    // Set the asynchronous task executor to be used for processing items concurrently.
    // This allows for concurrent processing of items, improving performance and throughput.
    // If not set, the step will process items sequentially.
    @Bean(name = "concurrencyStep")
    public Step concurrencyStep(JobRepository jobRepository,
                                RecordItemReaderWrapper itemReaderWrapper,
                                RecordItemProcessor itemProcessor,
                                RecordItemWriter writer,
                                AsyncTaskExecutor taskExecutor) {
        return new StepBuilder("Concurrency Step", jobRepository)
             .<Record, DbRecord>chunk(10)
             .reader(itemReaderWrapper)
             .processor(itemProcessor)
             .writer(writer)
             .taskExecutor(taskExecutor)
             .build();
    }
}