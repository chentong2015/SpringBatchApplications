package project.partitions;

import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import project.bean.Record;

import java.nio.file.FileSystems;
import java.nio.file.Path;

// 线程的Reader只读取特定的Partition切分文件
@Component
public class RecordItemPartitionReader extends StaxEventItemReader<Record> {

    // 读取文件时根据Step ExecutionContext获取对应的XML文件
    public RecordItemPartitionReader(@Value("#{stepExecutionContext['file']}") String partitionFilepath,
                                     @Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);

        System.out.println(Thread.currentThread().getName() + " Reader read: " + partitionFilepath);
        Path filepath = FileSystems.getDefault().getPath(partitionFilepath);
        setResource(new FileSystemResource(filepath));
        setFragmentRootElementNames(new String[] {"record"});
    }
}