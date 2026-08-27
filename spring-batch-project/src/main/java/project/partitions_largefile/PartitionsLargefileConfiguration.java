package project.partitions_largefile;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import project.common.bean.DbRecord;
import project.common.bean.Record;
import project.common.RecordItemProcessor;
import project.common.RecordItemWriter;
import project.partitions_largefile.bycount.FileSplitterByCountTasklet;

@Configuration
public class PartitionsLargefileConfiguration {

    // TODO. 先对文件进行拆分, 再执行差分文件的并发处理
    @Bean(name = "partitionsXMLJob")
    public Job job(JobRepository jobRepository, @Qualifier("preSplitStep") Step preSplitStep, @Qualifier("masterStep") Step masterStep) {
        return new JobBuilder("Partitions XML Job", jobRepository)
                .start(preSplitStep)
                .next(masterStep)
                .build();
    }

    @Bean(name = "preSplitStep")
    public Step preSplitStep(JobRepository jobRepository, FileSplitterByCountTasklet splitTasklet) {
        return new StepBuilder("Split File Step", jobRepository)
                .tasklet(splitTasklet)
                .build();
    }

    @Bean(name = "masterStep")
    public Step masterStep(JobRepository jobRepository, XmlFilePartitioner partitioner, ThreadPoolTaskExecutor taskExecutor,
                           @Qualifier("workerStep") Step workerStep) {
        return new StepBuilder("Master Step", jobRepository)
                .partitioner("Worker Step", partitioner)
                .taskExecutor(taskExecutor)
                .step(workerStep)
                .gridSize(4)
                .build();
    }

    // 独立处理每个Split文件的Step工作流
    @Bean(name = "workerStep")
    public Step workerStep(JobRepository jobRepository, XmlFilePartitionReader partitionReader, RecordItemProcessor itemProcessor, RecordItemWriter itemWriter) {
        return new StepBuilder("workerStep", jobRepository)
                .<Record, DbRecord>chunk(100)
                .reader(partitionReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }
}