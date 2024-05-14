package com.spring.batch.config;

public class MyBatchAutoConfiguration {

    // Spring Boot 自动装配如下的bean对象
    // [org/springframework/boot/autoconfigure/batch/BatchAutoConfiguration$SpringBootBatchConfiguration.class]
    //
    // @Bean(name = "jobRepository")
    // public JobRepository getJobRepository() throws Exception {
    //     JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
    //     factory.setDataSource(dataSource());
    //     factory.setTransactionManager(getTransactionManager());
    //     factory.afterPropertiesSet();
    //     return factory.getObject();
    // }
    //
    // @Bean(name = "transactionManager")
    // public PlatformTransactionManager getTransactionManager() {
    //    return new ResourcelessTransactionManager();
    // }
}
