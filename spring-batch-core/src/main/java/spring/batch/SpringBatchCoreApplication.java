package spring.batch;

import org.springframework.batch.core.job.parameters.JobParametersBuilder;
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
        // 连接DB的JobRepository类型是Proxy代理类型
        JobRepository jobRepository = context.getBean(JobRepository.class);
        JobOperator jobOperator = context.getBean(JobOperator.class);

        Job job = (Job) context.getBean("xmlToDbJob");
        JobExecution execution = jobOperator.start(job, getJobParameter());
        System.out.println("Job Status : " + execution.getStatus());
        System.out.println("Job completed");
        context.close();
    }

    // TODO. .preventRestart() 默认不允许同名JOB重复执行
    // 1. 每次为JOB提供不同的Parameter
    // 2. 为JOB添加RunIdIncrementer
    private static JobParameters getJobParameter() {
        return new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();
    }
}