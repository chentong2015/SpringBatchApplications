package spring.batch.csv2xml.process;

import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;

@Component
public class TransactionItemReader extends FlatFileItemReader<Transaction> {

    // Resource源资源来自于Classpath路径
    @Value("csv/transactions.csv")
    private Resource inputCsv;

    public TransactionItemReader() {
        super(null, null);
        setResource(inputCsv);
        setLineMapper(getLineMapper());
    }

    // 自定义CSV文件读取的Delimited分隔符标识, 用于FieldSetMapper解析时通过名称读取
    private LineMapper<Transaction> getLineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "id", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CsvFieldSetMapper());
        return lineMapper;
    }
}
