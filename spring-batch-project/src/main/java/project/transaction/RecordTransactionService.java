package project.transaction;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.sql.DataSource;
import java.util.List;

@Service
public class RecordTransactionService {

    private final CustomTransactionTemplate transactionTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RecordTransactionService(CustomTransactionTemplate transactionTemplate, DataSource dataSource) {
        this.transactionTemplate = transactionTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // TODO. 为Batch批提交添加编程式事务控制
    public void insert(List<String> records) {
        this.transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus status) {
                batchUpdate(records, "sql");
            }
        }, new CustomTransactionListenerImpl());
    }

    private void batchUpdate(List<?> list, String sql) {
        if (list != null && !list.isEmpty()) {
            SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(list.toArray());
            namedParameterJdbcTemplate.batchUpdate(sql, params);
        }
    }
}
