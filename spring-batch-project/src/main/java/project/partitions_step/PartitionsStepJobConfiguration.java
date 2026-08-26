package project.partitions_step;

import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import project.common.RecordItemReader;
import project.common.RecordItemWriter;
import project.common.bean.DbRecord;
import project.common.bean.Record;

@Configuration
public class PartitionsStepJobConfiguration {

    // TODO. 使用Partitioner划分，将整个Step划分多个subStep并发执行
    @Bean(name = "partitionStep")
    public Step partitionStep(JobRepository jobRepository,
                              ResourceFilePartitioner partitioner,
                              TaskExecutorPartitionHandler partitionHandler) {
        return new StepBuilder("partitionStep", jobRepository)
                .partitioner("Sub Step", partitioner)
                .partitionHandler(partitionHandler)
                .build();
    }

    // TODO. PartitionHandler分区处理器: 执行分区步骤的具体工作
    // 使分区步骤能够适配特定分布式计算环境的主要SPI(服务提供者接口)
    @Bean
    public TaskExecutorPartitionHandler partitionHandler(ThreadPoolTaskExecutor taskExecutor,  @Qualifier("subStep") Step subStep) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor);
        handler.setGridSize(4);
        handler.setStep(subStep);
        return handler;
    }

    @Bean(name = "subStep")
    public Step subStep(JobRepository jobRepository, RecordItemReader itemReader, RecordItemWriter itemWriter)  {
        return new StepBuilder("Sub Step", jobRepository)
                .<Record, DbRecord>chunk(3)
                .reader(itemReader)
                // .processor()
                .writer(itemWriter)
                .build();
    }
}