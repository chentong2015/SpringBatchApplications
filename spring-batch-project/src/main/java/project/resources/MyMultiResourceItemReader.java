package project.resources;

import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import project.common.bean.Record;

// 支持从多个资源中顺序读取数据
public class MyMultiResourceItemReader {

     private MultiResourceItemReader<Record> multiResourceItemReader;

     public MyMultiResourceItemReader() {
         Resource resource1 = new FileSystemResource("filepath1");
         Resource resource2 = new FileSystemResource("filepath2");
         this.multiResourceItemReader.setResources(new Resource[] {resource1, resource2});
     }
}