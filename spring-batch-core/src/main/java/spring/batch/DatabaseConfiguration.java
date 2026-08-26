package spring.batch;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JdbcJobRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration extends DefaultBatchConfiguration {

    // TODO. 普通数据库连接(无连接池控制), 每次都新创建连接
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://localhost:5432/spring_batch");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres");
        return dataSource;
    }

    @Bean
    public JobRepository jobRepository() throws BatchConfigurationException {
        try {
            JdbcJobRepositoryFactoryBean factory = new JdbcJobRepositoryFactoryBean();
            factory.setDataSource(dataSource());
            factory.setTransactionManager(getTransactionManager());
            factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception exception) {
            throw new BatchConfigurationException("Unable to configure the default job repository", exception);
        }
    }
}
