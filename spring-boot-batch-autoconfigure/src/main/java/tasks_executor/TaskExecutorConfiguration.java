package tasks_executor;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.batch.repeat.support.TaskExecutorRepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TaskExecutorConfiguration {

    // TODO. 通过线程池并发执行Step, 并发Step的共享数据必须保证线程安全
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        final int nbThreads = 4;
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(nbThreads);
        return taskExecutor;
    }

    @Bean
    public RepeatTemplate getRepeatTemplate() {
        TaskExecutorRepeatTemplate executorRepeatTemplate = new TaskExecutorRepeatTemplate();
        // executorRepeatTemplate.setThrottleLimit(taskExecutor().getCorePoolSize());
        executorRepeatTemplate.setTaskExecutor(taskExecutor());
        return executorRepeatTemplate;
    }

    // TODO. 通过两种方式设置Step并发执行的效果
    @Bean
    public Step step(JobRepository jobRepository, PlatformTransactionManager transactionManager, TaskExecutor taskExecutor) {
        return new StepBuilder("step1", jobRepository)
             .chunk(10, transactionManager)
             // .reader(reader)
             // .processor(processor)
             // .writer(writer)
             .taskExecutor(taskExecutor)
             .stepOperations(getRepeatTemplate())
             .build();
    }
}
