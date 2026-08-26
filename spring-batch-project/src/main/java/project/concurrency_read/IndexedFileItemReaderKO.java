package project.concurrency_read;

import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

// TODO. StaxEventItemReader类型不适合多线程调用.read()同一个文件
public class IndexedFileItemReaderKO<T> extends StaxEventItemReader<T> {

    private long startIndex;
    private long endIndex;
    private long currentIndex = 0;

    public IndexedFileItemReaderKO(Jaxb2Marshaller unmarshaller, long startIndex, long endIndex) {
        super(unmarshaller);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    // 多线程调用super.read()导致对同一个文件的读取游标位置异常
    // ItemStreamException: Error reading XML Stream
    @Override
    public T read() throws Exception {
        T item;
        while (true) {
            item = super.read();
            if (item == null) {
                return null;
            }

            long index = currentIndex++;
            if (index < startIndex) {
                continue; // skip
            }
            if (index >= endIndex) {
                return null; // stop this partition
            }
            return item;
        }
    }
}
