package project.partitions;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import project.bean.Record;

import java.nio.file.FileSystems;
import java.nio.file.Path;

// TODO. @StepScope 延迟到Step执行并创建Reader时调用构造器,获取上下文参数
@StepScope
@Component
public class RecordItemPartitionReader extends StaxEventItemReader<Record> {

    // 通过Step的ExecutionContext找到特定Partition切分文件
    public RecordItemPartitionReader(@Value("#{stepExecutionContext['file']}") String partitionFilepath,
                                     @Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);

        System.out.println(Thread.currentThread().getName() + " Reader read: " + partitionFilepath);
        Path filepath = FileSystems.getDefault().getPath(partitionFilepath);
        setResource(new FileSystemResource(filepath));
        setFragmentRootElementNames(new String[] {"record"});
    }
}