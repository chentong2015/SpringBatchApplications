package com.spring.batch.conversion.reader;

import com.spring.batch.model.Transaction;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class CsvFlatFileItemReader implements FactoryBean<ItemReader<Transaction>>  {

    // Resource资源来自于Classpath路径
    @Value("transactions.csv")
    private Resource inputCsv;

    @Override
    public ItemReader<Transaction> getObject() throws Exception {
        FlatFileItemReader<Transaction> reader = new FlatFileItemReader<>();
        reader.setResource(inputCsv);

        // 自定义CSV文件读取的Delimited分隔符标识, 用于FieldSetMapper解析时同通过名称读取
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setNames("username", "id", "date", "amount");

        DefaultLineMapper<Transaction> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(new CsvFieldSetMapper());
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Override
    public Class<?> getObjectType() {
        return CsvFlatFileItemReader.class;
    }
}
