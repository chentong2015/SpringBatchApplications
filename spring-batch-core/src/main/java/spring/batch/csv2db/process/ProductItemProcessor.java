package spring.batch.csv2db.process;

import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.stereotype.Component;
import spring.batch.csv2db.bean.Product;

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