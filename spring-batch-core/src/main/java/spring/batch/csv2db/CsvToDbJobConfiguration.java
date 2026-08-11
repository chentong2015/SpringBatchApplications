package spring.batch.csv2db;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.BindException;
import spring.batch.csv2db.bean.Product;
import spring.batch.csv2db.process.ProductItemProcessor;

@Configuration
public class CsvToDbJobConfiguration {

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("importUserJob1", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, ItemReader<Product> productItemReader, ProductItemProcessor productItemProcessor, ItemWriter<Product> productItemWriter) {
        return new StepBuilder("step1", jobRepository)
                .<Product, Product>chunk(2)
                .reader(productItemReader)
                .processor(productItemProcessor)
                .writer(productItemWriter)
                .build();
    }

    // 多CSV文件中读取Java对象数据
    @Bean(name = "productLineMapper")
    public LineMapper<Product> productLineMapper(FieldSetMapper<Product> productFieldSetMapper) {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("name", "value");

        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(productFieldSetMapper);
        return lineMapper;
    }

    @Bean
    public FieldSetMapper<Product> productFieldSetMapper() {
        return fieldSet -> {
            Product product = new Product();
            product.setName(fieldSet.readString(0));
            product.setValue(fieldSet.readString(1));
            return product;
        };
    }
}
