package database_to_file;

import filedata_to_db.model.Person;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

// TODO. Database也可以作为Batch Job指定的数据源
public class PersonJdbcReader {

    // 直接从DB中读取数据并mapper解析
    public ItemReader<Person> reader1(DataSource dataSource) {
        String query = "SELECT first_name, last_name FROM people";
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
        reader.setRowMapper(new PersonRowMapper());
        reader.setDataSource(dataSource);
        reader.setSql(query);
        return reader;
    }

    // 使用PreparedStatement动态生成查询语句, 并使用特定的RowMapper
    public ItemReader<Person> reader2(DataSource dataSource) {
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
        // reader.setRowMapper(new PersonRowMapper());
        reader.setDataSource(dataSource);
        reader.setSql(getSql());
        reader.setPreparedStatementSetter(getPreparedStatementSetter());
        return reader;
    }

    protected String getSql() {
        return "SELECT ID, ALTERNATE_ID FROM RECORD_CHECKSUM WHERE ORIGIN = ? AND STATUS <> ?";
    }

    protected PreparedStatementSetter getPreparedStatementSetter() {
        return ps -> {
            ps.setString(1, "AB");
            ps.setString(2, "OK");
        };
    }
}
