package spring.batch.csv2db.process;

import org.springframework.batch.infrastructure.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.infrastructure.item.database.JdbcBatchItemWriter;
import org.springframework.stereotype.Component;
import spring.batch.csv2db.bean.Product;

import javax.sql.DataSource;

@Component
public class ProductItemWriter extends JdbcBatchItemWriter<Product> {

    private String query = "INSERT INTO product (name, value) VALUES (:name, :value)";

    public ProductItemWriter(DataSource dataSource) {
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        setSql(query);
        setDataSource(dataSource);
    }
}