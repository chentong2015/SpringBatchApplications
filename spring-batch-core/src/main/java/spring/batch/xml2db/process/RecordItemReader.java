package spring.batch.xml2db.process;

import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import spring.batch.xml2db.bean.Record;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Component
public class RecordItemReader extends StaxEventItemReader<Record> {

    public RecordItemReader() throws Exception {
        super(null);
        setUnmarshaller(getUnmarshaller());
        setFragmentRootElementNames(new String[] {"record"});
    }

    // 配置XML文件的Schema格式和解析出来的Class类型
    private Unmarshaller getUnmarshaller() throws Exception {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setClassesToBeBound(Record.class);
        unmarshaller.setSchema(new ClassPathResource("xml/records.xml"));

        // Activate XML validation among other initializations
        unmarshaller.afterPropertiesSet();
        return unmarshaller;
    }
}
