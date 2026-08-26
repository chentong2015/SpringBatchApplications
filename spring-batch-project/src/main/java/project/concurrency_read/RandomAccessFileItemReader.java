package project.concurrency_read;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import project.common.bean.Record;

import java.io.RandomAccessFile;

@Component
@StepScope
public class RandomAccessFileItemReader implements ItemStreamReader<Record> {

    private RandomAccessFile file;
    private long startOffset;
    private long endOffset;

    // TODO. Step执行时获取上下文参数, 根据偏移量读取文件部分
    public RandomAccessFileItemReader(@Value("#{stepExecutionContext['startOffset']}") long startOffset,
                                      @Value("#{stepExecutionContext['endOffset']}") long endOffset) {
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    // 打开文件时自动跳转到Offset偏移量起始位置
    @Override
    public void open(ExecutionContext context) {
        try {
            file = new RandomAccessFile("drive_folder/xml/records.xml", "r");
            file.seek(startOffset);
        } catch (Exception exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    // 每个线程顺序读取各自偏移部分, 解析成Object对象结构
    @Override
    public Record read() throws Exception {
        if (file.getFilePointer() >= endOffset) {
            return null;
        }

        byte[] buffer = new byte[1024];
        file.read(buffer);

        String line = file.readLine();
        return new Record();
    }
}
