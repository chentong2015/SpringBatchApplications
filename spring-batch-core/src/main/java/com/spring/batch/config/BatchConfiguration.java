package com.spring.batch.config;

import com.spring.batch.condition.StepCondition;
import com.spring.batch.listener.MyJobExecutionListener;
import com.spring.batch.listener.MyStepExecutionListener;
import com.spring.batch.converter.RecordFieldSetMapper;
import com.spring.batch.converter.TransactionItemProcessor;
import com.spring.batch.converter.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.*;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

public class BatchConfiguration {

    // Resource资源来自于Classpath路径
    @Value("transactions.csv")
    private Resource inputCsv;

    // 当前项目的根目录的相对路径
    @Value("file:spring-batch-core/xml/output.xml")
    private Resource outputXml;

    // TODO. JobLauncher任务的启动需要在DB中记录数据，注入自定义的JobRepository
    @Bean(name = "myJobLauncher")
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean(name = "jobRepository")
    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager) throws Exception {
        JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
        factory.setDataSource(dataSource);
        factory.setTransactionManager(transactionManager);
        factory.afterPropertiesSet();
        return factory.getObject();
    }

    // TODO. 定义要执行的Job, 相同Job名称只会在BATCH_JOB_INSTANCE中记录一次
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
    protected Step step1(JobRepository jobRepository,
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

    @Bean
    public ItemReader<Transaction> itemReader() throws UnexpectedInputException, ParseException {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(inputCsv);

        // 自定义CSV文件读取的Delimited分隔符标识, 用于FieldSetMapper解析时同通过名称读取
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "userid", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new RecordFieldSetMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemWriter<Transaction> itemWriter() {
        StaxEventItemWriter<Transaction> itemWriter = new StaxEventItemWriter<>();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);

        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("transactionRecord");
        itemWriter.setResource((WritableResource) outputXml);
        return itemWriter;
    }
}
