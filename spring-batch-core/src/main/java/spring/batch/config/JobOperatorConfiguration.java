package spring.batch.config;

import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.TaskExecutorJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobOperatorConfiguration {

    // TODO. JobLauncher负责执行特定名称的Job，启动需要在DB中记录数据
    @Bean(name = "myJobOperator")
    public JobOperator jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobOperator jobLauncher = new TaskExecutorJobOperator();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}
