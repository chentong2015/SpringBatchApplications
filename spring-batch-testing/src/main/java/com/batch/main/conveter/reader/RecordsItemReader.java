package com.batch.main.conveter.reader;

import com.batch.main.model.Records;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;

@Component
public class RecordsItemReader implements ItemReader<Records> {

    private static final String XML_FILE_PATH = "records.xml";

    @Override
    public Records read() throws Exception {
        StaxEventItemReader<Records> reader = new StaxEventItemReader<>();
        reader.setFragmentRootElementNames(new String[] {"records"});
        reader.setUnmarshaller(getUnmarshaller());
        // reader.setResource(new FileSystemResource(XML_FILE_PATH));
        reader.setResource(new ClassPathResource(XML_FILE_PATH));
        return reader.read();
    }

    // 配置XML文件的Schema格式和解析出来的Class类型
    private Jaxb2Marshaller getUnmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Records.class);

        // Activetes XML validation among other initializations
        // unmarshaller.setSchema(new ClassPathResource(schemaLocation));
        // unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }
}
