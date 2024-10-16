package batch_auto_configure;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class BatchAutoConfigureCustom extends DefaultBatchConfiguration {

    @Autowired
    private DataSource dataSource;

    // TODO. 自定义注入JobRepository
    //  - 取消无用的相关设置
    //  - 修改默认的事务隔离级别"ISOLATION_SERIALIZABLE"以支持事务并发
    @Override
    public JobRepository jobRepository() throws BatchConfigurationException {
        try {
            JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
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
