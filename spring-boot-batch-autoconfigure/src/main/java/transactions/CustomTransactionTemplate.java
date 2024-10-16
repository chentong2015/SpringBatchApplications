package transactions;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

// 自定义事务执行的模板: 控制数据持久化事务的Callback和Listener
public class CustomTransactionTemplate {

    // 事务执行所关联的DataSource
    private DataSource dataSource;
    private TransactionTemplate transactionTemplate;

    // TODO. 设置事务执行的隔离级别和传播机制
    public CustomTransactionTemplate(DataSource dataSource) {
        this.dataSource = dataSource;

        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    public void execute(TransactionCallbackWithoutResult callback) {
        execute(callback, null);
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
