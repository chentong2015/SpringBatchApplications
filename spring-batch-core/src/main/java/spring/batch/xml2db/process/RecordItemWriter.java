package spring.batch.xml2db.process;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import spring.batch.xml2db.bean.DbRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordItemWriter implements ItemWriter<DbRecord> {

    private final RecordRepositoryService repositoryService;

    public RecordItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // TODO. 获取单批次处理的chunk, 确定操作数量
    @Override
    public void write(Chunk<? extends DbRecord> chunk) {
        System.out.println("Batch chunk: " + chunk.size());
        this.repositoryService.batchInsert((List<DbRecord>) chunk.getItems());
    }
}
