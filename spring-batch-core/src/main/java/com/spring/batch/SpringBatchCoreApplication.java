package com.spring.batch;

import com.spring.batch.config.BatchConfiguration;
import com.spring.batch.config.DbSourceConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBatchCoreApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(BatchConfiguration.class, DbSourceConfiguration.class);
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
