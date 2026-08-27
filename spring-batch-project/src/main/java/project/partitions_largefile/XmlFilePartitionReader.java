package project.partitions_largefile;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.stereotype.Component;
import project.common.bean.Record;

import java.nio.file.FileSystems;
import java.nio.file.Path;

// 先执行Partitioner划分操作, 设置Step执行提供上下文
// @StepScope 延迟到Step执行时注入, 创建Reader时获取ExecutionContext执行上下文(参数值)
@StepScope
@Component
public class XmlFilePartitionReader extends StaxEventItemReader<Record> {

    public XmlFilePartitionReader(@Value("#{stepExecutionContext['part-file']}") String partFilepath,
                                  @Qualifier("xmlUnmarshaller") Unmarshaller xmlUnmarshaller) throws Exception {
        super(xmlUnmarshaller);
        System.out.println(Thread.currentThread().getName() + " Reader read: " + partFilepath);

        Path filepath = FileSystems.getDefault().getPath(partFilepath);
        setResource(new FileSystemResource(filepath));
        setFragmentRootElementNames(new String[] {"record"});
    }
}