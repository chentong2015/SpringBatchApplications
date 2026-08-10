package spring.batch.csv_and_database.to_db;

import spring.batch.csv_and_database.bean.Person;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

@Configuration
public class PersonItemReader {

    @Bean
    public FlatFileItemReader<Person> read() {
        Resource resource = new ClassPathResource("csv/sample-data.csv");
        return new FlatFileItemReaderBuilder<Person>()
                .name("personItemReader")
                .resource(resource)
                .delimited()
                .names("firstName", "lastName")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Person.class);
                }})
                .build();
    }
}
