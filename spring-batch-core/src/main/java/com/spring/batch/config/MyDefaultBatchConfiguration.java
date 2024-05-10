package com.spring.batch.config;

import org.springframework.batch.core.configuration.BatchConfigurationException;
import org.springframework.batch.core.configuration.support.DefaultBatchConfiguration;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;

import javax.sql.DataSource;

// TODO. 重写Spring-batch-core默认注入的JobRepository
// 设置连接的Data Source以及隔离级别
public class MyDefaultBatchConfiguration extends DefaultBatchConfiguration {

    private DataSource dataSource;

    @Override
    public JobRepository jobRepository() throws BatchConfigurationException {
        try {
            JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
            factory.setDataSource(dataSource);
            factory.setTransactionManager(getTransactionManager());
            factory.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");
            factory.afterPropertiesSet();
            return factory.getObject();
        } catch (Exception var3) {
            throw new BatchConfigurationException("Unable to configure the default job repository", var3);
        }
    }
}
