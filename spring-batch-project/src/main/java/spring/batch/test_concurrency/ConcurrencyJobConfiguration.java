package spring.batch.test_concurrency;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Configuration
public class ConcurrencyJobConfiguration {

    // TODO. 通过线程池并发执行Step, 并发Step的共享数据必须保证线程安全
    @Bean
    public AsyncTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(8);
        return taskExecutor;
    }

    // @Bean(name = "concurrencyJob")
    // public Job concurrencyJob(JobRepository jobRepository, @Qualifier("concurrencyStep") Step concurrencyStep) {
    //     return new JobBuilder("Concurrency Job", jobRepository)
    //             .preventRestart()
    //             .start(concurrencyStep)
    //             .build();
    // }
    //
    // @Bean(name = "concurrencyStep")
    // public Step concurrencyStep(JobRepository jobRepository, AsyncTaskExecutor taskExecutor) {
    //     return new StepBuilder("Concurrency Step", jobRepository)
    //          .chunk(10)
    //          // .reader(reader)
    //          // .processor(processor)
    //          // .writer(writer)
    //          .taskExecutor(taskExecutor)
    //          .build();
    // }

    // 读取XML文件: 配置Schema XSD格式, 验证提交的文件数据符合规范
    @Bean(name = "xmlUnmarshaller")
    public Unmarshaller xmlUnmarshaller() throws Exception {
        Path filepath = FileSystems.getDefault().getPath("drive_folder/xml/records.xsd");

        // JAXB只负责把xml数据反序列化成Java对象
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Record.class);
        unmarshaller.setSchema(new FileSystemResource(filepath));

        // Activate XML validation among other initializations
        unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }
}
