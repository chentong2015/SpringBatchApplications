package spring.batch.csv_and_database.to_csv;

import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;
import spring.batch.csv_and_database.bean.Person;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

@Configuration
public class PersonItemReader {

    // 直接从DB中读取数据并mapper解析到Java对象
    @Bean
    public ItemReader<Person> read(DataSource dataSource) {
        String query = "SELECT first_name, last_name FROM people";
        RowMapper<Person> rowMapper = new PersonRowMapper();
        return new JdbcCursorItemReader<>(dataSource, query, rowMapper);
    }

    // 使用PreparedStatement动态生成查询语句, 并使用特定的RowMapper
    public ItemReader<Person> testReader(DataSource dataSource) {
        String query = "SELECT ID, ALTERNATE_ID FROM CHECKSUM WHERE ORIGIN = ? AND STATUS <> ?";
        RowMapper<Person> rowMapper = new PersonRowMapper();
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>(dataSource, query, rowMapper);
        reader.setPreparedStatementSetter(getPreparedStatementSetter());
        return reader;
    }

    protected PreparedStatementSetter getPreparedStatementSetter() {
        return ps -> {
            ps.setString(1, "AB");
            ps.setString(2, "OK");
        };
    }
}
