package spring.batch.xml2db;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;

import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.xml2db.bean.DbRecord;
import spring.batch.xml2db.bean.Record;
import spring.batch.xml2db.process.RecordItemProcessor;

@Configuration
public class XmlToDbConfiguration {

    @Bean(name = "loadXmlToDbJob")
    public Job job(JobRepository jobRepository, Step importRecords) {
        return new JobBuilder("loadXmlToDbJob", jobRepository)
                .preventRestart()
                .start(importRecords)
                .build();
    }

    // TODO. 读取XmL中的<record标签并解析成DbRecord对象
    // 以chunk块的方式进行批量操作并写入到数据库中
    @Bean
    public Step importRecords(JobRepository jobRepository,
                              ItemReader<Record> reader,
                              RecordItemProcessor itemProcessor,
                              ItemWriter<DbRecord> writer) {
        return new StepBuilder("importRecords", jobRepository)
                .<Record, DbRecord>chunk(10)
                .reader(reader)
                .processor(itemProcessor)
                .writer(writer)
                .build();
    }
}
