package database_to_csv.process;

import beans.Person;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

@Configuration
public class PersonItemReader {

    // 直接从DB中读取数据并mapper解析
    @Bean
    public ItemReader<Person> read(DataSource dataSource) {
        String query = "SELECT first_name, last_name FROM people";
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
        reader.setRowMapper(new PersonRowMapper());
        reader.setDataSource(dataSource);
        reader.setSql(query);
        return reader;
    }

    // 使用PreparedStatement动态生成查询语句, 并使用特定的RowMapper
    public ItemReader<Person> testReader(DataSource dataSource) {
        String query = "SELECT ID, ALTERNATE_ID FROM CHECKSUM WHERE ORIGIN = ? AND STATUS <> ?";
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql(query);
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
