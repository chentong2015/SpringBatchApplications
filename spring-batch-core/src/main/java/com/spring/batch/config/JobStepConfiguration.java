package com.spring.batch.config;

import com.spring.batch.condition.StepCondition;
import com.spring.batch.listener.MyJobExecutionListener;
import com.spring.batch.listener.MyStepExecutionListener;
import com.spring.batch.conversion.processor.TransactionItemProcessor;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class JobStepConfiguration {

    // TODO. JobLauncher任务的启动需要在DB中记录数据，注入自定义的JobRepository
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
    @Bean(name = "loadCsvToXmlJob")
    public Job job(JobRepository jobRepository, @Qualifier("step1") Step step1) {
        return new JobBuilder("loadCsvToXmlJob", jobRepository)
                .preventRestart()
                .listener(MyJobExecutionListener.class)
                .start(step1)
                .next(step1)
                .build();
    }

    // TODO. 同一个Job可以按照属性执行不同的Step(Read, Process, Write)
    @Bean
    @Conditional(StepCondition.class)
    public Step step1(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         ItemReader<Transaction> reader,
                         ItemWriter<Transaction> writer) {
        return new StepBuilder("step1", jobRepository)
                .listener(MyStepExecutionListener.class)
                .<Transaction, Transaction>chunk(10, transactionManager)
                .reader(reader)
                .processor(new TransactionItemProcessor())
                .writer(writer)
                .build();
    }
}
