package csv_to_xml.config;

import csv_to_xml.listener.MyJobExecutionListener;
import csv_to_xml.listener.MyTaskletStepListener;
import csv_to_xml.processing.WorkTaskletCleanFiles;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaskletJobStepConfiguration {

    @Bean(name = "taskletJob")
    public Job job(JobRepository jobRepository, Step firstEmptyStep, Step nextTaskletStep) {
        return new JobBuilder("taskletJob3", jobRepository)
                .preventRestart()
                .listener(MyJobExecutionListener.class)
                .flow(firstEmptyStep)
                .next(nextTaskletStep)
                .end()
                .build();
    }

    @Bean(name = "firstEmptyStep")
    public Step firstEmptyStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Empty Step", jobRepository)
                .allowStartIfComplete(true)
                .tasklet((StepContribution sc, ChunkContext cc) -> RepeatStatus.FINISHED, transactionManager)
                .build();
    }

    @Bean(name = "nextTaskletStep")
    public Step nextTaskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        // 删除项目构建的/target结果目录中/clean的文件
        Resource resource = new ClassPathResource("clean/");
        WorkTaskletCleanFiles taskletCleanFiles = new WorkTaskletCleanFiles(resource);

        return new StepBuilder("deleteFilesInDir", jobRepository)
                .tasklet(taskletCleanFiles, transactionManager)
                .listener(new MyTaskletStepListener())
                .build();
    }
}
