package spring.batch.test_concurrency.process;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import spring.batch.test_concurrency.bean.DbRecord;

import java.util.List;

@Component
public class RecordItemWriter implements ItemWriter<DbRecord> {

    private final RecordRepositoryService repositoryService;

    public RecordItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // TODO. 写入时获取单批次处理的chunk, 确定操作数量
    @Override
    public void write(Chunk<? extends DbRecord> chunk) {
        System.out.println("Batch chunk: " + chunk.size());
        this.repositoryService.batchInsert((List<DbRecord>) chunk.getItems());
    }
}
