package csv_to_database.config.tasks;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class TaskExecutorConfiguration {

    // TODO. 通过线程池并发执行Step, 并发Step的共享数据必须保证线程安全
    @Bean
    public TaskExecutor taskExecutor() {
        final int nbThreads = 4;
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(nbThreads);
        return taskExecutor;
    }
}
