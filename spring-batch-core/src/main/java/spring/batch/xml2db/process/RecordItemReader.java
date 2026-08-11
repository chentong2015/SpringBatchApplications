package spring.batch.xml2db.process;

import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import spring.batch.xml2db.bean.Record;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;

@Component
public class RecordItemReader extends StaxEventItemReader<Record> {

    public RecordItemReader(Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);
        setFragmentRootElementNames(new String[] {"record"});
    }
}
