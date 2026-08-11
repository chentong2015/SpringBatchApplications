package spring.batch.csv2db.process;

import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.LineMapper;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import spring.batch.csv2db.bean.Product;

import java.nio.file.FileSystems;
import java.nio.file.Path;

@Component
public class ProductItemReader extends FlatFileItemReader<Product> {

    public ProductItemReader(LineMapper<Product> productLineMapper) {
        super(productLineMapper);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/csv/productData.csv");
        setResource(new FileSystemResource(filepath));

        setLinesToSkip(1);
    }
}