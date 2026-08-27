package spring.batch.common.transaction;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;

import javax.sql.DataSource;
import java.util.List;
import java.util.function.Consumer;

// TODO. 为Batch批提交添加编程式事务控制
@Service
public class RecordTransactionService {

    private final CustomTransactionTemplate transactionTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RecordTransactionService(CustomTransactionTemplate transactionTemplate, DataSource dataSource) {
        this.transactionTemplate = transactionTemplate;
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void insert(List<String> records) {
        this.transactionTemplate.execute(new Consumer<TransactionStatus>() {
            @Override
            public void accept(TransactionStatus transactionStatus) {
                batchUpdate(records, "sql");
            }
        }, new CustomTransactionListener());
    }

    private void batchUpdate(List<?> list, String sql) {
        if (list != null && !list.isEmpty()) {
            SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(list.toArray());
            namedParameterJdbcTemplate.batchUpdate(sql, params);
        }
    }
}
