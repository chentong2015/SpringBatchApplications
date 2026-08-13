package project.base_process;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;
import project.base_bean.DbRecord;

import javax.sql.DataSource;
import java.util.List;

@Service
public class RecordRepositoryService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RecordRepositoryService(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    // BatchInsert插入只发送一个SQL到数据库
    public void batchInsert(List<DbRecord> recordDBList) {
        String sql = "insert into record_db (username, id, amount) VALUES (:username, :id, :amount)";
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(recordDBList);
        this.namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
