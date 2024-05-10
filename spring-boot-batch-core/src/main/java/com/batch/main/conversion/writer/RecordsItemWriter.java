package com.batch.main.conversion.writer;

import com.batch.main.dao.RecordRepositoryService;
import com.batch.main.model.DbRecord;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordsItemWriter implements ItemWriter<DbRecord>  {

    private final RecordRepositoryService repositoryService;

    public RecordsItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void write(Chunk<? extends DbRecord> chunk) throws Exception {
        System.out.println("Batch chunk: " + chunk.size());
        this.repositoryService.batchInsert((List<DbRecord>) chunk.getItems());
    }
}
