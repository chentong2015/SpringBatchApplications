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
        // 连接DB的JobRepository类型
        JobRepository jobRepository = context.getBean(JobRepository.class);
        JobOperator jobOperator = context.getBean(JobOperator.class);

        // 选择特定的JOB来执行
        Job job = (Job) context.getBean("csvToXmlJob");
        JobExecution execution = jobOperator.start(job, new JobParameters());
        System.out.println("Job Status : " + execution.getStatus());
        System.out.println("Job completed");
    }
}