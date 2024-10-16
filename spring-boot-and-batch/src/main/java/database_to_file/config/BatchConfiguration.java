package database_to_file.config;

import database_to_file.processing.PersonItemProcessor;
import database_to_file.model.Person;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchConfiguration {

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/test_batch_core");
        return dataSource;
    }

    @Bean
    public Job retrieveUserJob(JobRepository jobRepository, Step retrieveUserStep) {
        return new JobBuilder("Retrieve User Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(retrieveUserStep)
                .end()
                .build();
    }

    @Bean
    public Step retrieveUserStep(JobRepository jobRepository,
                                 PlatformTransactionManager transactionManager,
                                 ItemReader<Person> reader,
                                 PersonItemProcessor processor,
                                 ItemWriter<Person> writer) {
        return new StepBuilder("Retrieve User from DB", jobRepository)
                .<Person, Person>chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
