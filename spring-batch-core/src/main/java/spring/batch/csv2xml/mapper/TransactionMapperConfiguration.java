package spring.batch.csv2xml.mapper;

import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import spring.batch.csv2xml.bean.Transaction;

@Configuration
public class TransactionMapperConfiguration {

    // 自定义CSV文件读取的Delimited分隔符标识, 用于FieldSetMapper解析时通过名称读取
    @Bean(name = "transactionLineMapper")
    public LineMapper<Transaction> transactionLineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "id", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CsvFieldSetMapper());
        return lineMapper;
    }

    // 写入到XML文件的Marshaller编制器
    @Bean
    public Jaxb2Marshaller transactionMarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);
        return marshaller;
    }
}
