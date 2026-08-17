package spring.batch.xml2db.process;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import spring.batch.xml2db.bean.Record;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;

import java.nio.file.FileSystems;
import java.nio.file.Path;

// TODO. 从XML取特定标签映射到Object对象
// StaxEventItemReader底层是有状态的XML Stream Reader, 必须按照顺序读取
// FragmentRoot标记根标签, 允许文件中不存在<record>标签, 则不执行Process和Write !!
@Component
public class RecordItemReader extends StaxEventItemReader<Record> {

    public RecordItemReader(@Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);

        Path filepath = FileSystems.getDefault().getPath("drive_folder/xml/records.xml");
        setResource(new FileSystemResource(filepath));
        setFragmentRootElementNames(new String[] {"record"});
    }
}
