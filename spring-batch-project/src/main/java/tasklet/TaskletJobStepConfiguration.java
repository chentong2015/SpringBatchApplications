package tasklet;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
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

    // 删除项目构建的/target结果目录中/clean的文件
    @Bean(name = "nextTaskletStep")
    public Step nextTaskletStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Resource resource = new ClassPathResource("clean/");
        WorkTaskletCleanFiles taskletCleanFiles = new WorkTaskletCleanFiles(resource);
        return new StepBuilder("deleteFilesInDir", jobRepository)
                .tasklet(taskletCleanFiles, transactionManager)
                .build();
    }
}