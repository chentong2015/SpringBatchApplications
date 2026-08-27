package spring.batch.common.transaction;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.function.Consumer;

// TODO. TransactionTemplate.execute()编程式事务控制
@Component
public class CustomTransactionTemplate {

    private final TransactionTemplate transactionTemplate;

    // 设置事务执行的隔离级别和传播机制
    public CustomTransactionTemplate(DataSource dataSource) {
        DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
        transactionTemplate = new TransactionTemplate(txManager);
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
    }

    // 为事务的执行添加Listener监听器控制
    // Execute the action specified by the given Runnable within a transaction.
    public void execute(Consumer<TransactionStatus> action, CustomTransactionListener listener) {
        if (listener == null) {
            throw new RuntimeException("listener can't be null");
        }

        listener.beforeTransaction();
        this.transactionTemplate.executeWithoutResult(action);
        listener.afterTransaction();
    }
}
