package spring.batch.common.multi_resources;

import org.springframework.batch.infrastructure.item.file.MultiResourceItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

// 支持从多个资源中顺序读取数据
public class MyMultiResourceItemReader {

     private MultiResourceItemReader<Entity> multiResourceItemReader;

     public MyMultiResourceItemReader() {
         Resource resource1 = new FileSystemResource("filepath1");
         Resource resource2 = new FileSystemResource("filepath2");
         this.multiResourceItemReader.setResources(new Resource[] {resource1, resource2});
     }

     class Entity {
         private int id;
         private String value;
     }
}