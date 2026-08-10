package spring.batch.xml_to_database.process;

import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import spring.batch.xml_to_database.repository.RecordRepositoryService;
import spring.batch.xml_to_database.bean.DbRecord;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordsItemWriter implements ItemWriter<DbRecord> {

    private final RecordRepositoryService repositoryService;

    public RecordsItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void write(Chunk<? extends DbRecord> chunk) {
        System.out.println("Batch chunk: " + chunk.size());
        this.repositoryService.batchInsert((List<DbRecord>) chunk.getItems());
    }
}
