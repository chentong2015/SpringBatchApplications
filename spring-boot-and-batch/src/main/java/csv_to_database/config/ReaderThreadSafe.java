package csv_to_database.config;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

// 在Step中使用线程安全地Reader来获取数据
public class ReaderThreadSafe<T> implements ItemStreamReader<T>, InitializingBean {

    private ItemReader<T> itemReader;
    private boolean isStream = false;

    public void setItemReader(ItemReader<T> itemReader) {
        this.itemReader = itemReader;
        if (itemReader instanceof ItemStream) {
            isStream = true;
        }
    }

    // TODO. 包装Reader在并发情况数据的被有序地读取
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
