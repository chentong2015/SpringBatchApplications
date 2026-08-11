package spring.batch;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBatchProjectApplication {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplication(SpringBatchProjectApplication.class).run();
        JobOperator jobOperator = context.getBean(JobOperator.class);

        Job job = (Job) context.getBean("taskletJob");
        JobExecution execution = jobOperator.start(job, new JobParameters());
        System.out.println("Job Status : " + execution.getStatus());
        System.out.println("Job completed");
    }
}
