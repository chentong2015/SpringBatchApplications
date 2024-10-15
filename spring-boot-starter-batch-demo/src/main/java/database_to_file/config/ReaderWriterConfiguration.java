package database_to_file.config;

import database_to_file.bean.Person;
import database_to_file.conversion.PersonRowMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

@Configuration
public class ReaderWriterConfiguration {

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

    // 将读取的数据写入指定的文件
    @Bean
    public ItemWriter<Person> write() {
        // FlatFileItemWriter<Person> fileItemWriter = new FlatFileItemWriter<>();
        // fileItemWriter.setLineSeparator();
        return persons -> persons.forEach(System.out::println);
    }
}
