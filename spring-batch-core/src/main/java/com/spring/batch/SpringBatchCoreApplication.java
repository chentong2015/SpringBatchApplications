package com.spring.batch;

import com.spring.batch.config.JobStepConfiguration;
import com.spring.batch.config.DbSourceConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBatchCoreApplication {

    // TODO. 自定义要注入的Configuration, 不会自动装配DefaultBatchConfiguration中的bean
    //
    // Spring Boot autoconfigure会自动加载如下的Configuration, 导致bean冲突
    // org.springframework.batch.core.configuration.support.DefaultBatchConfiguration
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(JobStepConfiguration.class, DbSourceConfiguration.class);
        context.refresh();

        // TODO. 通过JobLauncher执行指定的Job任务
        JobLauncher jobLauncher = (JobLauncher) context.getBean("myJobLauncher");
        Job job = (Job) context.getBean("loadCsvToXmlJob");
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
