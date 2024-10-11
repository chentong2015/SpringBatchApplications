package com.batch.main.config;

import com.batch.main.model.Record;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ReaderConfiguration {

    @Bean
    public ItemReader<Record> recordReader() throws Exception {
        StaxEventItemReader<Record> reader = new StaxEventItemReader<>();
        reader.setUnmarshaller(getUnmarshaller());
        // 设置读取的XML文件的Fragment标签(片段的标签)
        reader.setFragmentRootElementNames(new String[] {"record"});

        // new FileSystemResource("spring-batch-testing/src/main/resources/records.xml")
        reader.setResource(new ClassPathResource("records.xml"));
        return reader;
    }

    // 配置XML文件的Schema格式和解析出来的Class类型
    private Jaxb2Marshaller getUnmarshaller() throws Exception {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Record.class);
        // unmarshaller.setSchema(new ClassPathResource(schemaLocation));

        // Activetes XML validation among other initializations
        unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }
}
