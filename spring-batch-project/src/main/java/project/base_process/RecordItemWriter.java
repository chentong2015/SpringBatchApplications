package project.base_process;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.stereotype.Component;
import project.base_bean.DbRecord;

import java.util.List;

@Component
public class RecordItemWriter implements ItemWriter<DbRecord> {

    private final RecordRepositoryService repositoryService;

    public RecordItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    // TODO. 多线程并发执行写入操作
    @Override
    public void write(Chunk<? extends DbRecord> chunk) {
        System.out.println(Thread.currentThread().getName() + "write batch chunk: " + chunk.size());
        try {
            Thread.sleep(5000);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }
        this.repositoryService.batchInsert((List<DbRecord>) chunk.getItems());
    }
}
