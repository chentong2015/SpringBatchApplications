package csv_to_xml;

import csv_to_xml.config.BatchConfiguration;
import csv_to_xml.config.ChunkJobStepConfiguration;
import csv_to_xml.config.ReaderWriterConfiguration;
import csv_to_xml.config.TaskletJobStepConfiguration;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBatchCoreApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.register(BatchConfiguration.class);
        appContext.register(ChunkJobStepConfiguration.class);
        appContext.register(ReaderWriterConfiguration.class);
        appContext.register(TaskletJobStepConfiguration.class);
        appContext.refresh();

        JobLauncher jobLauncher = (JobLauncher) appContext.getBean("myJobLauncher");
        Job job = (Job) appContext.getBean("taskletJob");
        try {
            JobExecution execution = jobLauncher.run(job, new JobParameters());
            System.out.println("Job Status : " + execution.getStatus());
            System.out.println("Job completed");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
