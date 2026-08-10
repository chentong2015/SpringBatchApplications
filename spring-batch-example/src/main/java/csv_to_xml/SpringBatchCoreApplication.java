package csv_to_xml;

import csv_to_xml.chunk.ChunkJobStepConfiguration;
import csv_to_xml.chunk.TransactionItemWriter;
import csv_to_xml.tasklet.TaskletJobStepConfiguration;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringBatchCoreApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        appContext.register(BatchConfiguration.class);
        appContext.register(ChunkJobStepConfiguration.class);
        appContext.register(TransactionItemWriter.class);
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
