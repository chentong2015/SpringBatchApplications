package spring.batch.csv_and_database.to_db;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import spring.batch.csv_and_database.bean.Product;

@Component
public class ProductItemProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(final Product product) {
        Product transformedProduct = new Product(product.getName().toUpperCase(),
                product.getValue().toUpperCase());

        System.out.println("Converting (" + product + ") into (" + transformedProduct + ")");
        return transformedProduct;
    }
}