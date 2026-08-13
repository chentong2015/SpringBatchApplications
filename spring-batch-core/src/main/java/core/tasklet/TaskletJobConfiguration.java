package core.tasklet;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


@Configuration
public class TaskletJobConfiguration {

    @Bean(name = "taskletJob")
    public Job taskletJob(JobRepository jobRepository,
                          @Qualifier("firstEmptyStep") Step firstEmptyStep,
                          @Qualifier("nextTaskletStep") Step nextTaskletStep) {
        return new JobBuilder("Tasklet Job", jobRepository)
                .preventRestart()
                .flow(firstEmptyStep)
                .next(nextTaskletStep)
                .end()
                .build();
    }

    @Bean(name = "firstEmptyStep")
    public Step firstEmptyStep(JobRepository jobRepository) {
        return new StepBuilder("First Empty Step", jobRepository)
                .allowStartIfComplete(true)
                .tasklet((StepContribution sc, ChunkContext cc) -> RepeatStatus.FINISHED)
                .build();
    }

    @Bean(name = "nextTaskletStep")
    public Step nextTaskletStep(JobRepository jobRepository) {
        Resource resource = new ClassPathResource("clean/");
        DeleteFileTasklet taskletCleanFiles = new DeleteFileTasklet(resource);
        return new StepBuilder("Second Delete Files Step", jobRepository)
                .tasklet(taskletCleanFiles)
                .build();
    }
}