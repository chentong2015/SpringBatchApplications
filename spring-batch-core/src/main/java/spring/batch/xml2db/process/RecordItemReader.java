package spring.batch.xml2db.process;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import spring.batch.xml2db.bean.Record;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;

@Component
public class RecordItemReader extends StaxEventItemReader<Record> {

    public RecordItemReader(@Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);
        setFragmentRootElementNames(new String[] {"record"});
    }
}
