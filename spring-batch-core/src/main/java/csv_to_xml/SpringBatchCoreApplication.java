package csv_to_xml;

import csv_to_xml.config.DataSourceConfiguration;
import csv_to_xml.config.JobStepConfiguration;
import csv_to_xml.config.ReaderWriterConfiguration;
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
        appContext.register(ReaderWriterConfiguration.class);
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
