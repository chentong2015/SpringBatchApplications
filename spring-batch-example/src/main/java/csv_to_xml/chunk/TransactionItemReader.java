package csv_to_xml.chunk;

import csv_to_xml.mapper.CsvFieldSetMapper;
import beans.Transaction;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class TransactionItemReader {

    // Resource源资源来自于Classpath路径
    @Value("csv/transactions.csv")
    private Resource inputCsv;

    @Bean
    public ItemReader<Transaction> itemReader() {
        // 自定义CSV文件读取的Delimited分隔符标识, 用于FieldSetMapper解析时同通过名称读取
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "id", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CsvFieldSetMapper());

        return new FlatFileItemReader<>(inputCsv, lineMapper);
    }
}
