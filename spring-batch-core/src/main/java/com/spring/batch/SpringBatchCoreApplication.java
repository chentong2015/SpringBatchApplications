package com.spring.batch;

import com.spring.batch.config.ConversionConfiguration;
import com.spring.batch.config.DataSourceConfiguration;
import com.spring.batch.config.JobStepConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBatchCoreApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.register(DataSourceConfiguration.class);
        appContext.register(JobStepConfiguration.class);
        appContext.register(ConversionConfiguration.class);
        appContext.refresh();

        JobLauncher jobLauncher = (JobLauncher) appContext.getBean("myJobLauncher");
        Job job = (Job) appContext.getBean("convertCsvToXmlJob");
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
