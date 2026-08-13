package project.concurrency;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.support.SynchronizedItemStreamReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import project.base_bean.DbRecord;
import project.base_bean.Record;
import project.base_process.RecordItemProcessor;
import project.base_process.RecordItemReader;
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

    // TODO. taskExecutor()无法让chunk操作流并发执行
    @Bean(name = "concurrencyStep")
    public Step concurrencyStep(JobRepository jobRepository,
                                RecordItemReader itemReader,
                                RecordItemProcessor itemProcessor,
                                RecordItemWriter itemWriter,
                                ThreadPoolTaskExecutor taskExecutor) {
        return new StepBuilder("Concurrency Step", jobRepository)
                .<Record, DbRecord>chunk(100)
                .reader(new SynchronizedItemStreamReader<>(itemReader))
                .processor(itemProcessor)
                .writer(itemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    // TODO. .tasklet()并发执行效果
    // @Bean(name = "concurrencyStep")
    // public Step concurrencyStep(JobRepository jobRepository, ChunkParallelTasklet chunkParallelTasklet) {
    //     return new StepBuilder("Concurrency Step", jobRepository)
    //             .tasklet(chunkParallelTasklet)
    //             .build();
    // }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);
        taskExecutor.setMaxPoolSize(4);
        taskExecutor.setThreadNamePrefix("Batch Thread -");
        return taskExecutor;
    }

    // 读取XML文件: 配置Schema XSD格式, 验证提交的文件数据符合规范
    @Bean(name = "xmlUnmarshaller")
    public Unmarshaller xmlUnmarshaller() throws Exception {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Record.class);
        unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }
}