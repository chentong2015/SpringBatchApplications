package com.spring.batch.conversion;

import com.spring.batch.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// JDBC RowMapper: ResultSet -> Object 从查询结果Row映射到Class对象
public class PersonRowMapper implements RowMapper<Person> {

    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Person(rs.getString(1), rs.getString(2));
    }
}
