package project.common;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import project.bean.DbRecord;

import java.util.List;

@Component
public class RecordItemWriter implements ItemWriter<DbRecord> {

    private final RecordRepositoryService repositoryService;

    public RecordItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void write(Chunk<? extends DbRecord> chunk) {
        System.out.println(Thread.currentThread().getName() + " write batch chunk: " + chunk.size());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        this.repositoryService.batchInsert((List<DbRecord>) chunk.getItems());
    }
}
