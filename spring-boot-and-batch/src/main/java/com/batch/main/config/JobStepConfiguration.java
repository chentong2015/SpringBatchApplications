package com.batch.main.config;

import com.batch.main.conversion.RecordItemProcessor;
import com.batch.main.model.DbRecord;
import com.batch.main.model.Record;
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
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

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
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager manager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(manager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    // TODO. SpringBoot启动后会默认执行Job对应的Step, 可通过属性配置关闭
    // 在应用启动后，更加参数条件来选择执行Job和特定的Step
    @Bean(name = "loadXmlToDbJob30")
    public Job job(JobRepository jobRepository, Step step) {
        return new JobBuilder("loadXmlToDbJob30", jobRepository)
                .preventRestart()
                .start(step)
                .build();
    }

    // TODO. 读取XmL中的<record标签并解析成DbRecord对象
    // 以chunk块的方式进行批量操作并写入到数据库中
    @Bean
    public Step importRecords(JobRepository jobRepository,
                         PlatformTransactionManager transactionManager,
                         ItemReader<Record> reader,
                         ItemWriter<DbRecord> writer) {
        return new StepBuilder("importRecords", jobRepository)
                .<Record, DbRecord>chunk(10, transactionManager)
                .reader(reader)
                .processor(new RecordItemProcessor())
                .writer(writer)
                .build();
    }
}
