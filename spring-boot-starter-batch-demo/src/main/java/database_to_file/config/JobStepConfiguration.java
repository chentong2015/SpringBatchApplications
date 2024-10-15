package database_to_file.config;

import database_to_file.bean.Person;
import database_to_file.conversion.PersonItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

@Configuration
public class JobStepConfiguration {

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
