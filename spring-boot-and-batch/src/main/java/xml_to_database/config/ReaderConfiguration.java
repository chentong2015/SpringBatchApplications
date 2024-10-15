package xml_to_database.config;

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
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        // 配置XML文件的Schema格式和解析出来的Class类型
        unmarshaller.setClassesToBeBound(Record.class);
        unmarshaller.setSchema(new ClassPathResource("records.xml"));
        // Activetes XML validation among other initializations
        unmarshaller.afterPropertiesSet();

        StaxEventItemReader<Record> reader = new StaxEventItemReader<>();
        reader.setUnmarshaller(unmarshaller);
        // 设置读取的XML文件的Fragment标签(片段的标签)
        reader.setFragmentRootElementNames(new String[] {"record"});
        return reader;
    }
}
