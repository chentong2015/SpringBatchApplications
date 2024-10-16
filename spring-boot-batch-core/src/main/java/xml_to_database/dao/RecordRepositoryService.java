package xml_to_database.dao;

import xml_to_database.model.DbRecord;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

// TODO. DAO层数据持久化所使用的DataSource和Spring JobRepository所使用的DB可能不同
@Service
public class RecordRepositoryService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    // 持久化到指定的Database
    public RecordRepositoryService(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void batchInsert(List<DbRecord> recordDBList) {
        String sql = "Insert into record_db (username, id, amount) VALUES (:username, :id, :amount)";
        SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(recordDBList);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }
}
