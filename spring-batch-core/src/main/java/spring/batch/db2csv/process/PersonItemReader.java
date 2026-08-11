package spring.batch.db2csv.process;

import org.springframework.batch.infrastructure.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import spring.batch.db2csv.bean.Person;
import org.springframework.batch.infrastructure.item.ItemReader;
import spring.batch.db2csv.mapper.PersonRowMapper;

import javax.sql.DataSource;

// 直接从DB中读取数据并mapper解析到Java对象
@Component
public class PersonItemReader extends JdbcCursorItemReader<Person> {

    private String query = "SELECT first_name, last_name FROM people";

    public PersonItemReader(DataSource dataSource) {
        super(dataSource, null, new PersonRowMapper());
        setSql(query);
    }

    // 测试: 生成带参数的PreparedStatement动态查询语句
    public ItemReader<Person> testReader(DataSource dataSource) {
        String query = "SELECT ID, ALTERNATE_ID FROM CHECKSUM WHERE ORIGIN = ? AND STATUS <> ?";
        RowMapper<Person> rowMapper = new PersonRowMapper();
        JdbcCursorItemReader<Person> reader = new JdbcCursorItemReader<>(dataSource, query, rowMapper);
        reader.setPreparedStatementSetter(ps -> {
            ps.setString(1, "AB");
            ps.setString(2, "OK");
        });
        return reader;
    }
}
