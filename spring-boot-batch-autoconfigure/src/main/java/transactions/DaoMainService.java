package transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;

@Service
public class DaoMainService {

    @Autowired
    private CustomTransactionTemplate transactionTemplate;

    private NamedParameterJdbcTemplate jdbcTemplate;

    public DaoMainService(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // 使用控制批量数据插入的相关事务
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
            jdbcTemplate.batchUpdate(sql, params);
        }
    }
}
