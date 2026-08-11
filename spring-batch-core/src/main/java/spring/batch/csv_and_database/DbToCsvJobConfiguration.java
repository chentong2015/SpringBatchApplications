package spring.batch.csv_and_database;

import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import spring.batch.csv_and_database.bean.Person;
import spring.batch.csv_and_database.to_csv.PersonItemProcessor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DbToCsvJobConfiguration {

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
                                 ItemReader<Person> reader,
                                 PersonItemProcessor processor,
                                 ItemWriter<Person> writer) {
        return new StepBuilder("Retrieve User from DB", jobRepository)
                .<Person, Person>chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
