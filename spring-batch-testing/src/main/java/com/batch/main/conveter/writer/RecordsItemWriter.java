package com.batch.main.conveter.writer;

import com.batch.main.dao.RecordRepositoryService;
import com.batch.main.model.RecordsDB;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecordsItemWriter implements ItemWriter<RecordsDB>  {

    private final RecordRepositoryService repositoryService;

    public RecordsItemWriter(RecordRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    @Override
    public void write(Chunk<? extends RecordsDB> chunk) throws Exception {
        System.out.println("Batch chunk: " + chunk.size());
        this.repositoryService.batchInsert((List<RecordsDB>) chunk.getItems());
    }
}
