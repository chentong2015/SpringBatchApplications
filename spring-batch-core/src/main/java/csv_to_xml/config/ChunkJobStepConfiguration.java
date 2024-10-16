package csv_to_xml.config;

import csv_to_xml.processing.TransactionItemProcessor;
import csv_to_xml.listener.MyJobExecutionListener;
import csv_to_xml.listener.MyStepExecutionListener;
import csv_to_xml.model.Transaction;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ChunkJobStepConfiguration {

    // TODO. 定义要执行的Job, 相同Job名称只会在BATCH_JOB_INSTANCE中记录一次 !!
    @Bean(name = "convertCsvToXmlJob")
    public Job job(JobRepository jobRepository, Step convertStep) {
        return new JobBuilder("convertCsvToXmlJob2", jobRepository)
                .preventRestart()
                .listener(MyJobExecutionListener.class)
                .start(convertStep)
                .build();
    }

    // TODO. 定义Step执行步骤: Reader -> Processor -> Writer
    @Bean(name = "convertStep")
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
