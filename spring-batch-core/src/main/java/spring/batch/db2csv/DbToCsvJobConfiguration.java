package spring.batch.db2csv;

import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.RowMapper;
import spring.batch.db2csv.bean.Person;
import spring.batch.db2csv.process.PersonItemProcessor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class DbToCsvJobConfiguration {

    @Bean(name = "dbToCsvJob")
    public Job dbToCsvJob(JobRepository jobRepository, @Qualifier("dbToCsvStep") Step dbToCsvStep) {
        return new JobBuilder("Db to Csv Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(dbToCsvStep)
                .end()
                .build();
    }

    @Bean(name = "dbToCsvStep")
    public Step dbToCsvStep(JobRepository jobRepository,
                            ItemReader<Person> personItemReader,
                            PersonItemProcessor personItemProcessor,
                            ItemWriter<Person> personItemWriter) {
        return new StepBuilder("Db To Csv Step", jobRepository)
                .<Person, Person>chunk(3)
                .reader(personItemReader)
                .processor(personItemProcessor)
                .writer(personItemWriter)
                .build();
    }

    // 从DB多查询数据的RowMapper到Object对象
    @Bean
    public RowMapper<Person> personRowMapper() {
        return (rs, rowNum) -> new Person(rs.getString(1), rs.getString(2));
    }

    // 从Object对象上获取field属性值写入CSV文件中
    @Bean(name = "csvLineAggregator")
    public LineAggregator<Person> csvLineAggregator() {
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"firstName", "lastName"});

        // 设置CSV文件一行数据的格式分隔符
        DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return lineAggregator;
    }
}
