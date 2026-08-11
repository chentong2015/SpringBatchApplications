package spring.batch.csv_and_database.to_csv;

import org.springframework.context.annotation.Configuration;
import spring.batch.csv_and_database.bean.Person;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

@Configuration
public class PersonItemWriter {

    @Value("file:spring-batch-example/src/main/resources/csv/personsOutput.csv")
    private Resource outputCsv;

    // 将读取的数据写入指定的文件, 依次批量处理Chunk的数据量
    @Bean
    public ItemWriter<Person> write() {
        FlatFileItemWriter<Person> fileItemWriter = new FlatFileItemWriter<>(getCsvLineAggregator());
        fileItemWriter.setResource((WritableResource) outputCsv);

        // All job repetitions should "append" to same output file
        fileItemWriter.setAppendAllowed(true);
        return fileItemWriter;
    }

    // 定义如何从Object对象上获取field属性
    // Name field values sequence based on object properties
    private LineAggregator<Person> getCsvLineAggregator() {
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"lastName", "firstName"});

        // 设置CSV文件一行数据的格式分隔符
        DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return lineAggregator;
    }
}
