package xml_to_database.process;

import beans.xml.Record;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ReaderConfiguration {

    // 配置XML文件的Schema格式和解析出来的Class类型
    @Bean
    public ItemReader<Record> recordReader() throws Exception {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Record.class);
        unmarshaller.setSchema(new ClassPathResource("xml/records.xml"));
        // Activetes XML validation among other initializations
        unmarshaller.afterPropertiesSet();

        // 设置读取的XML文件的Fragment标签(片段的标签)
        StaxEventItemReader<Record> reader = new StaxEventItemReader<>(unmarshaller);
        reader.setFragmentRootElementNames(new String[] {"record"});
        return reader;
    }
}
