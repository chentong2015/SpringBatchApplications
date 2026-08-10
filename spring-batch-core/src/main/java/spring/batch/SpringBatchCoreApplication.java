package spring.batch;

import org.springframework.batch.core.launch.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;

@SpringBootApplication
public class SpringBatchCoreApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplication(SpringBatchCoreApplication.class).run();
        JobRepository jobRepository = context.getBean(JobRepository.class); //
        JobOperator jobOperator = context.getBean(JobOperator.class);

        Job job = (Job) context.getBean("taskletJob");
        JobExecution execution = jobOperator.start(job, new JobParameters());
        System.out.println("Job Status : " + execution.getStatus());
        System.out.println("Job completed");
    }
}
