package spring.batch.csv2xml.listener;

import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.stereotype.Component;

// JOB执行的监听器，在执行前后执行额外操作
@Component
public class MyJobExecutionListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();
        System.out.println(jobName);
    }

    // 判断JOB执行参数，统计时间，释放资源，日志监控等
    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("=== After JOB ===");
        System.out.println(jobExecution.getStatus());
        System.out.println(jobExecution.getJobParameters().getLong("timestamp"));
    }
}