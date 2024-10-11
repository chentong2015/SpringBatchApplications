package com.spring.batch.config;

import com.spring.batch.listener.MyJobExecutionListener;
import com.spring.batch.listener.MyStepExecutionListener;
import com.spring.batch.conversion.TransactionItemProcessor;
import com.spring.batch.model.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class JobStepConfiguration {

    // TODO. JobLauncher负责执行特定名称的Job，启动需要在DB中记录数据
    @Bean(name = "myJobLauncher")
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Primary
    @Bean(name = "jobRepository")
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    // TODO. 定义要执行的Job, 相同Job名称只会在BATCH_JOB_INSTANCE中记录一次 !!
    @Bean(name = "convertCsvToXmlJob")
    public Job job(JobRepository jobRepository, Step convertStep) {
        return new JobBuilder("convertCsvToXmlJob1", jobRepository)
                .preventRestart()
                .listener(MyJobExecutionListener.class)
                .start(convertStep)
                // .next(nextStep) Job任务可执行多个不同Step
                .build();
    }

    // TODO. 定义Step具体执行的步骤: Reader -> Processor -> Writer
    @Bean
    public Step convertStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            ItemReader<Transaction> reader,
                            ItemWriter<Transaction> writer) {
        return new StepBuilder("convertCsvToXmlStep", jobRepository)
                .listener(MyStepExecutionListener.class)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(reader)
                .processor(new TransactionItemProcessor())
                .writer(writer)
                .build();
    }
}
