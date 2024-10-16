package csv_to_xml.config;

import csv_to_xml.processing.CsvFieldSetMapper;
import csv_to_xml.model.Transaction;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ReaderWriterConfiguration {

    // Resource源资源来自于Classpath路径
    @Value("transactions.csv")
    private Resource inputCsv;

    // WritableResource输出资源的路径定位在项目路径下
    @Value("file:spring-batch-core/src/main/resources/output.xml")
    private Resource outputXml;

    @Bean
    public ItemReader<Transaction> itemReader() {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(inputCsv);

        // 自定义CSV文件读取的Delimited分隔符标识, 用于FieldSetMapper解析时同通过名称读取
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "id", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);

        lineMapper.setFieldSetMapper(new CsvFieldSetMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean
    public ItemWriter<Transaction> itemWriter() {
        StaxEventItemWriter<Transaction> itemWriter = new StaxEventItemWriter<>();
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setClassesToBeBound(Transaction.class);

        itemWriter.setMarshaller(marshaller);
        itemWriter.setRootTagName("transactionRecord");
        itemWriter.setResource((WritableResource) outputXml);
        return itemWriter;
    }
}
