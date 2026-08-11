package spring.batch.json2json;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.parameters.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.WritableResource;
import spring.batch.json2json.bean.Trade;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Configuration
public class JsonToJsonConfiguration {

    @Bean(name = "json2JsonJob")
    public Job json2JsonJob(JobRepository jobRepository, @Qualifier("json2JsonStep") Step json2JsonStep) {
        return new JobBuilder("Json to Json Job", jobRepository)
                .incrementer(new RunIdIncrementer())
                .flow(json2JsonStep)
                .end()
                .build();
    }

    @Bean(name = "json2JsonStep")
    public Step json2JsonStep(JobRepository jobRepository,
                              JsonItemReader<Trade> jsonDataReader,
                              ItemProcessor<Trade, Trade> jsonDataProcessor,
                              ItemWriter<Trade> jsonDataWriter) {
        return new StepBuilder("Json to Json Step", jobRepository)
                .<Trade, Trade>chunk(3)
                .reader(jsonDataReader)
                .processor(jsonDataProcessor)
                .writer(jsonDataWriter)
                .build();
    }

    @Bean(name = "jsonWritableResource")
    public WritableResource jsonWritableResource() {
        Path filepath = FileSystems.getDefault().getPath("drive_folder/json/tradesOutput.json");
        return new FileSystemResource(filepath);
    }
}