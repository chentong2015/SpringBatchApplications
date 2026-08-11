package spring.batch.csv2db.mapper;

import org.springframework.batch.infrastructure.item.file.mapping.FieldSetMapper;
import org.springframework.batch.infrastructure.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import spring.batch.csv2db.bean.Product;

public class CsvFieldSetMapper implements FieldSetMapper<Product> {

    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
        Product product = new Product();
        product.setName(fieldSet.readString(0));
        product.setValue(fieldSet.readString(1));
        return product;
    }
}
