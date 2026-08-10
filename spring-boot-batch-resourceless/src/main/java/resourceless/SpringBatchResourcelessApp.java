package resourceless;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

// TODO. Spring Boot Batch
// 默认自动装配注入ResourcelessJobRepository
// 无需存储Batch Metadata数据, 无数据库连接
@SpringBootApplication
public class SpringBatchResourcelessApp {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = new SpringApplication(SpringBatchResourcelessApp.class).run();
        JobRepository jobRepository = context.getBean(JobRepository.class); // ResourcelessJobRepository
        JobOperator jobOperator = context.getBean(JobOperator.class);

        Job job = context.getBean("sampleJobName", Job.class);
        jobOperator.start(job, new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters());
    }
}