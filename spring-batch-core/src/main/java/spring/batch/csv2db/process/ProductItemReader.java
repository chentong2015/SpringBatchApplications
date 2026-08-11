package spring.batch.csv2db.process;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2db.bean.Product;

@Component
public class ProductItemReader extends FlatFileItemReader<Product> {

    public ProductItemReader(LineMapper<Product> productLineMapper) {
        super(new ClassPathResource("csv/productData.csv"), productLineMapper );
        setLinesToSkip(1);
    }
}