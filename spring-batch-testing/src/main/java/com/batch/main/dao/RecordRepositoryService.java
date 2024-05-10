package com.batch.main.dao;

import com.batch.main.model.RecordDB;
import com.batch.main.model.RecordsDB;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

// 提供数据库持久化层的Dao API
@Service
public class RecordRepositoryService {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public RecordRepositoryService(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public void batchInsert(List<RecordsDB> recordsDBList) {
        String sql = "Insert into record_db (username, id, amount) VALUES (:username, :id, :amount)";
        for (RecordsDB recordsDB: recordsDBList) {
            List<RecordDB> recordDBList = recordsDB.getRecordDBList();
            SqlParameterSource[] params = SqlParameterSourceUtils.createBatch(recordDBList);
            namedParameterJdbcTemplate.batchUpdate(sql, params);
        }
    }
}
