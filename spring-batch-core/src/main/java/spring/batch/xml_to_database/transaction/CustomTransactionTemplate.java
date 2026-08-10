package spring.batch.xml_to_database.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

// TODO. TransactionTemplate.execute() 编程式事务控制
// - 配置数据持久化事务的Callback和Listener
// - 设置事务执行的隔离级别和传播机制
@Component
public class CustomTransactionTemplate {

    private TransactionTemplate transactionTemplate;

    public CustomTransactionTemplate(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    public void execute(TransactionCallbackWithoutResult callback, CustomTransactionListener listener) {
        if (listener == null) {
            throw new RuntimeException("listener can't be null");
        }
        listener.beforeTransaction();
        this.transactionTemplate.execute(callback);
        listener.afterTransaction();
    }
}
