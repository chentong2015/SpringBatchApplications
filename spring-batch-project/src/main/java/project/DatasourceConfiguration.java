package project;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JdbcJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatasourceConfiguration extends DefaultBatchConfiguration {

    // TODO. 配置支撑高并发CRUD的数据库连接池
    @Bean
    public DataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/spring_batch");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");

        dataSource.setMaximumPoolSize(4 * 2 + 1);
        dataSource.setMinimumIdle(4);
        dataSource.setConnectionTimeout(60000);
        dataSource.setMaxLifetime(580000);
        return dataSource;
    }

    // 注入JobRepository并设置事务隔离级别, 具体隔离级别可由TransactionTemplate控制
    @Bean
    public JobRepository jobRepository(DataSource dataSource) throws BatchConfigurationException {
        try {
            JdbcJobRepositoryFactoryBean factory = new JdbcJobRepositoryFactoryBean();
            factory.setDataSource(dataSource);
            factory.setTransactionManager(getTransactionManager());
            factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception exception) {
            throw new BatchConfigurationException("Unable to configure the default job repository", exception);
        }
    }
}
