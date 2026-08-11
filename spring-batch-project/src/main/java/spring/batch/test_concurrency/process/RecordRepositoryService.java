package spring.batch.test_concurrency.process;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import spring.batch.test_concurrency.bean.DbRecord;

import javax.sql.DataSource;
import java.util.List;

// TODO. DAO层数据持久化所使用的DataSource和JobRepository数据库可能不同
@Service
public class RecordRepositoryService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 持久化到指定的Database
    public RecordRepositoryService(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void batchInsert(List<DbRecord> recordDBList) {
        String sql = "insert into record_db (username, id, amount) VALUES (:username, :id, :amount)";
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(recordDBList);
        this.namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
