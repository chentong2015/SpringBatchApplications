package spring.batch.csv_and_database.to_db;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import spring.batch.csv_and_database.bean.Product;

@Configuration
public class ProductItemReader {

    private Resource resource = new ClassPathResource("csv/productData.csv");

    @Bean
    public FlatFileItemReader<Product> read() {
        return new FlatFileItemReaderBuilder<Product>()
                .name("productItemReader")
                .resource(resource)
                .delimited()
                .names("name", "value")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(Product.class);
                }})
                .build();
    }
}
