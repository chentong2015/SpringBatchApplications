package project.base_process;

import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import project.base_bean.Record;

import java.nio.file.FileSystems;
import java.nio.file.Path;

// TODO. XML Stream流式读取不适合多线程共享并发读取
@Component
public class RecordItemReader extends StaxEventItemReader<Record> {

    public RecordItemReader(@Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/xml/records.xml");
        setResource(new FileSystemResource(filepath));
        setFragmentRootElementNames(new String[] {"record"});
    }
}