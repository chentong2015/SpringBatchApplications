package project.base_process;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import project.base_bean.Record;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemStream;
import org.springframework.batch.infrastructure.item.ItemStreamReader;

// TODO. 包装ItemReader在并发场景下保证数据读取安全
@Component
public class RecordItemReaderWrapper implements ItemStreamReader<Record> {

    private ItemReader<Record> itemReader;
    private boolean isStream = false;

    public RecordItemReaderWrapper(@Qualifier("recordItemReader") ItemReader<Record> itemReader) {
        this.itemReader = itemReader;
        if (itemReader instanceof ItemStream) {
            isStream = true;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) {
        if (isStream) {
            ((ItemStream) itemReader).open(new ExecutionContext());
        }
    }

    // 对数据的读取做同步化操作, 多影响影响较小(Processor和Writer才是耗时部分)
    @Override
    public synchronized Record read() throws Exception {
        return itemReader.read();
    }

    @Override
    public void update(ExecutionContext executionContext) {
    }

    @Override
    public void close() {
        if (isStream) {
            ((ItemStream) itemReader).close();
        }
    }
}
