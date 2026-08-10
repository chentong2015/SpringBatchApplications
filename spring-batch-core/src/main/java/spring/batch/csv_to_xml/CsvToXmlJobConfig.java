package spring.batch.csv_to_xml;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import spring.batch.csv_to_xml.bean.Transaction;
import spring.batch.csv_to_xml.process.TransactionItemProcessor;
import spring.batch.csv_to_xml.listener.MyJobExecutionListener;
import spring.batch.csv_to_xml.listener.MyStepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CsvToXmlJobConfig {

    // TODO. 定义要执行的Job, 相同Job名称只会在BATCH_JOB_INSTANCE中记录一次
    @Bean(name = "convertCsvToXmlJob")
    public Job job(JobRepository jobRepository, Step convertStep) {
        return new JobBuilder("convertCsvToXmlJob", jobRepository)
                .preventRestart()
                .listener(MyJobExecutionListener.class)
                .start(convertStep)
                .build();
    }

    @Bean(name = "convertStep")
    public Step convertStep(JobRepository jobRepository,
                            ItemReader<Transaction> reader,
                            TransactionItemProcessor itemProcessor,
                            ItemWriter<Transaction> writer) {
        return new StepBuilder("convertCsvToXmlStep", jobRepository)
                .listener(MyStepExecutionListener.class)
                .<Transaction, Transaction>chunk(10)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}
