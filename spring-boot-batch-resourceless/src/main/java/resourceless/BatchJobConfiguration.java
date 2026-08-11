package resourceless;

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

@Configuration
public class BatchJobConfiguration {

    @Bean("sampleJobName")
    public Job samplejob(JobRepository jobRepository, @Qualifier("firstEmptyStep") Step firstEmptyStep) {
        return new JobBuilder("tasklet Job", jobRepository)
                .preventRestart()
                .flow(firstEmptyStep)
                .end()
                .build();
    }

    @Bean(name = "firstEmptyStep")
    public Step firstEmptyStep(JobRepository jobRepository ) {
        return new StepBuilder("Empty step", jobRepository)
                .allowStartIfComplete(true)
                .tasklet((StepContribution sc, ChunkContext cc) -> {
                    System.out.println("Run empty step finished.");
                    return RepeatStatus.FINISHED;
                }).build();
    }
}
