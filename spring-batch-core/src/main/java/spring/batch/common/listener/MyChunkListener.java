package spring.batch.common.listener;

import org.springframework.batch.core.listener.ChunkListener;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.stereotype.Component;
import spring.batch.csv2xml.bean.Transaction;

@Component
public class MyChunkListener implements ChunkListener<Transaction, Transaction> {

    @Override
    public void beforeChunk(Chunk<Transaction> chunk) {
        System.out.println("Start reading chunk size: " + chunk.size());
    }

    @Override
    public void afterChunk(Chunk<Transaction> chunk) {
        System.out.println("Finish chunk size: " + chunk.size());
    }
}
