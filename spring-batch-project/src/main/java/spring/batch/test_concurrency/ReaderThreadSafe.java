package spring.batch.test_concurrency;

import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemStream;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

// TODO. 包装ItemReader在并发场景下保证数据读取安全
public class ReaderThreadSafe<T> implements ItemStreamReader<T>, InitializingBean {

    private ItemReader<T> itemReader;
    private boolean isStream = false;

    public void setItemReader(ItemReader<T> itemReader) {
        this.itemReader = itemReader;
        if (itemReader instanceof ItemStream) {
            isStream = true;
        }
    }

    @Override
    public synchronized T read() throws Exception {
        return itemReader.read();
    }

    @Override
    public void close() {
        if (isStream) {
            ((ItemStream) itemReader).close();
        }
    }

    @Override
    public void open(ExecutionContext executionContext) {
        if (isStream) {
            ((ItemStream) itemReader).open(new ExecutionContext());
        }
    }

    @Override
    public void update(ExecutionContext executionContext) {
    }

    @Override
    public void afterPropertiesSet() {
        Assert.notNull(this.itemReader, "A delegate item reader is required");
    }
}
