package project.partitions;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileSplitHolder implements Closeable {

    private static final byte[] TITLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ROOT_START = "<records>".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ROOT_END = "\n</records>".getBytes(StandardCharsets.UTF_8);
    private static final byte[] RECORD_END = "</record>".getBytes(StandardCharsets.UTF_8);
    private int match = 0; // 检测</record>匹配字节位置

    private OutputStream outputStream;
    private static final int BUFFER_SIZE = 1024 * 1024;
    private final Path outputDir = Path.of("drive_folder/xml/parts");

    private final int partitionNum;
    private final long partitionSize;
    private int currentPartNum = 1;
    private long currentPartSize = 0;

    public FileSplitHolder(int partitionNum, long partitionSize) throws IOException {
        this.partitionNum = partitionNum;
        this.partitionSize = partitionSize;
        openNewPartition();
    }

    // 创建新的Partition文件并初始化, 第一个拆分文件不需填标签
    private void openNewPartition() throws IOException {
        Path file = outputDir.resolve("part-" + currentPartNum + ".xml");
        outputStream = new BufferedOutputStream(Files.newOutputStream(
                file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), BUFFER_SIZE);
        if (currentPartNum > 1) {
            outputStream.write(TITLE);
            outputStream.write(ROOT_START);
        }
        currentPartSize += TITLE.length + ROOT_START.length;
    }

    // 找到完整</record>标签则批量写入, 再判断是否达到拆分文件大小
    public void process(byte[] buffer, int length) throws IOException {
        int offset = 0;
        for (int i = 0; i < length; i++) {
            if (buffer[i] == RECORD_END[match]) {
                match++;
                if (match == RECORD_END.length) {
                    write(buffer, offset, i + 1 - offset);
                    offset = i + 1;
                    match = 0;
                    if (currentPartNum < partitionNum && currentPartSize >= partitionSize) {
                        completePartition();
                        openNewPartition();
                    }
                }
            } else {
                match = (buffer[i] == RECORD_END[0]) ? 1 : 0;
            }
        }
        if (offset < length) {
            write(buffer, offset, length - offset);
        }
    }

    private void write(byte[] buffer, int offset, int length) throws IOException {
        outputStream.write(buffer, offset, length);
        currentPartSize += length;
    }

    private void completePartition() throws IOException {
        outputStream.write(ROOT_END);
        outputStream.close();
        currentPartNum++;
        currentPartSize = 0;
    }

    // 确保关闭最后一个拆分文件的输出流
    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }
}