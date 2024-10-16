package batch_auto_configure;

import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class BatchAutoConfigure {

    // TODO. 最少情况下，只需要配置JobRepository连接的DataSource即可运行
    // 其它Spring Batch相关的Beans全部被默认注入
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUsername("postgres");
        dataSource.setPassword("");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/test_batch_core");
        return dataSource;
    }

    // TODO. 以下为DefaultBatchConfiguration自动注入的Bean
    @Autowired
    private PlatformTransactionManager transactionManager;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobExplorer jobExplorer;

    @Autowired
    private JobOperator jobOperator;

}
