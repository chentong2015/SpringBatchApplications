package spring.batch.csv_and_database.to_db;

import org.springframework.batch.infrastructure.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spring.batch.csv_and_database.bean.Product;

import javax.sql.DataSource;

@Configuration
public class ProductItemWriter {

    @Bean
    public JdbcBatchItemWriter<Product> write(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Product>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO product (name, value) VALUES (:name, :value)")
                .dataSource(dataSource)
                .build();
    }
}
