package spring.batch.csv2xml;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.validation.BindException;
import spring.batch.csv2xml.bean.Transaction;
import spring.batch.csv2xml.listener.MyItemProcessorListener;
import spring.batch.csv2xml.process.TransactionItemProcessor;
import spring.batch.csv2xml.listener.MyJobExecutionListener;
import spring.batch.csv2xml.listener.MyStepExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class CsvToXmlJobConfiguration {

    @Bean(name = "convertCsvToXmlJob")
    public Job job(JobRepository jobRepository, Step convertStep) {
        return new JobBuilder("convertCsvToXmlJob", jobRepository)
                .preventRestart()
                .listener(MyJobExecutionListener.class) // Job Listener
                .start(convertStep)
                .build();
    }

    @Bean(name = "convertStep")
    public Step convertStep(JobRepository jobRepository,
                            ItemReader<Transaction> reader,
                            TransactionItemProcessor itemProcessor,
                            ItemWriter<Transaction> writer) {
        return new StepBuilder("convertCsvToXmlStep", jobRepository)
                .listener(MyStepExecutionListener.class) // Step Listener
                .<Transaction, Transaction>chunk(3)
                .reader(reader)
                .processor(itemProcessor)
                .listener(MyItemProcessorListener.class) // Item Listener
                .writer(writer)
                .build();
    }

    // CSV文件读取: Delimited分隔符标识, 用于FieldSetMapper解析时通过名称读取
    @Bean(name = "transactionLineMapper")
    public LineMapper<Transaction> transactionLineMapper(FieldSetMapper<Transaction> transactionFieldSetMapper) {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "id", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(transactionFieldSetMapper);
        return lineMapper;
    }

    // TODO. 解析Csv文件的FieldSet数据到Java对象
    @Bean
    public FieldSetMapper<Transaction> transactionFieldSetMapper() {
        return fieldSet -> {
            Transaction transaction = new Transaction();
            transaction.setUsername(fieldSet.readString("username"));
            transaction.setid(fieldSet.readInt(1));
            transaction.setAmount(fieldSet.readDouble(3));

            // 必须严格匹配Date日期的格式化，才能解析并读取
            String dateString = fieldSet.readString(2);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            transaction.setDate(LocalDate.parse(dateString, formatter));
            return transaction;
        };
    }

    // 输出到XML文件: 定义Marshaller编制器
    @Bean
    public Jaxb2Marshaller transactionMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);
        return marshaller;
    }
}
