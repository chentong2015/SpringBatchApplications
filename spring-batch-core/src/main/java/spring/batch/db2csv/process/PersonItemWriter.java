package spring.batch.db2csv.process;

import org.springframework.stereotype.Component;
import spring.batch.db2csv.bean.Person;
import org.springframework.batch.infrastructure.item.file.FlatFileItemWriter;
import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.WritableResource;

@Component
public class PersonItemWriter extends FlatFileItemWriter<Person> {

    @Value("file:spring-batch-example/src/main/resources/csv/personsOutput.csv")
    private Resource outputCsv;

    public PersonItemWriter() {
        super(null);
        setResource((WritableResource) outputCsv);
        setLineAggregator(getCsvLineAggregator());
        // All job repetitions should "append" to same output file
        setAppendAllowed(true);
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
