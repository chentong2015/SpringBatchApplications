package spring.batch.csv2db.process;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.batch.infrastructure.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.infrastructure.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2db.bean.Product;

@Component
public class ProductItemReader extends FlatFileItemReader<Product> {

    public ProductItemReader() {
        super(new ClassPathResource("csv/productData.csv"), null);
        setLinesToSkip(1);
        setLineMapper(getLineMapper());
    }

    private LineMapper<Product> getLineMapper() {
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("name", "value");

        DefaultLineMapper<Product> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CsvFieldSetMapper());
        return lineMapper;
    }
}