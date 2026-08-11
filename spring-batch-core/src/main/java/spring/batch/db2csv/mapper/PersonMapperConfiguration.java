package spring.batch.db2csv.mapper;

import org.springframework.batch.infrastructure.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.infrastructure.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.db2csv.bean.Person;

@Configuration
public class PersonMapperConfiguration {

    // 从Object对象上获取field属性值输出到CSV文件中
    @Bean(name = "csvLineAggregator")
    public LineAggregator<Person> csvLineAggregator() {
        BeanWrapperFieldExtractor<Person> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"lastName", "firstName"});

        // 设置CSV文件一行数据的格式分隔符
        DelimitedLineAggregator<Person> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter(",");
        lineAggregator.setFieldExtractor(fieldExtractor);
        return lineAggregator;
    }
}
