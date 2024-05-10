package com.batch.main.config;

import com.batch.main.conveter.processor.RecordItemProcessor;
import com.batch.main.model.Record;
import com.batch.main.model.RecordDB;
import com.batch.main.model.Records;
import com.batch.main.model.RecordsDB;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

// TODO. SpringBoot启动后会默认执行Job对应的Step
@Configuration
public class JobStepConfiguration {

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

    @Bean(name = "loadXmlToDbJob2")
    public Job job(JobRepository jobRepository, @Qualifier("importRecords") Step step) {
        return new JobBuilder("loadXmlToDbJob2", jobRepository)
                .preventRestart()
                .start(step)
                .build();
    }

    @Bean(name = "importRecords")
    protected Step importRecords(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         ItemReader<Records> reader,
                         ItemWriter<RecordsDB> writer) {
        return new StepBuilder("importRecords", jobRepository)
                .<Records, RecordsDB>chunk(10, transactionManager)
                .reader(reader)
                .processor(new RecordItemProcessor())
                .writer(writer)
                .build();
    }
}
