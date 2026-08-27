package project.concurrency_read.KO;

import org.springframework.batch.infrastructure.item.xml.StaxEventItemReader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

// TODO. StaxEventItemReader类型不适合多线程调用.read()同一个文件
public class RangeXmlItemReaderKO<T> extends StaxEventItemReader<T> {

    private long startIndex;
    private long endIndex;
    private long currentIndex = 0;

    public RangeXmlItemReaderKO(Jaxb2Marshaller unmarshaller, long startIndex, long endIndex) {
        super(unmarshaller);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    // TODO. 多线程并发导致对同个文件
    // Read游标位置异常，无法按照各自RangedIndex来读取: Error reading XML Stream
    @Override
    public T read() throws Exception {
        T item;
        while (true) {
            item = super.read();
            if (item == null) {
                // 整个Step的Reader生命周期结束, 不再读取数据
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
