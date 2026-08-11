package spring.batch.xml2db.process;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import spring.batch.xml2db.bean.Record;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;

import java.nio.file.FileSystems;
import java.nio.file.Path;

// 输入并从指定的XML文件中读取数据
@Component
public class RecordItemReader extends StaxEventItemReader<Record> {

    public RecordItemReader(@Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/xml/records.xml");
        setResource(new FileSystemResource(filepath));
        setFragmentRootElementNames(new String[] {"record"});
    }
}
