package database_to_csv.config;

import database_to_csv.model.Person;
import database_to_csv.processing.PersonRowMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.sql.DataSource;

@Configuration
public class ReaderWriterConfiguration {

    @Value("file:spring-boot-batch-core/src/main/output.csv")
    private Resource outputCsv;

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

    // 将读取的数据写入指定的文件, 依次批量处理Chunk的数据量
    @Bean
    public ItemWriter<Person> write() {
        FlatFileItemWriter<Person> fileItemWriter = new FlatFileItemWriter<>();
        fileItemWriter.setResource((WritableResource) outputCsv);

        // All job repetitions should "append" to same output file
        fileItemWriter.setAppendAllowed(true);

        //Name field values sequence based on object properties
        fileItemWriter.setLineAggregator(getCsvLineAggregator());
        return fileItemWriter;
    }

    private LineAggregator<Person> getCsvLineAggregator() {
        // 定义如何从Object对象上获取field属性
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"lastName", "firstName"});

        // 设置CSV文件一行数据的格式分隔符
        DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return lineAggregator;
    }
}
