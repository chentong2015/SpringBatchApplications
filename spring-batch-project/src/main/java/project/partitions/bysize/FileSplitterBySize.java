package project.partitions.bysize;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// TODO. 基于文件的大小拆分成均匀大小的文件
public class FileSplitterBySize implements Closeable {

    private static final byte[] TITLE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ROOT_START = "<records>".getBytes(StandardCharsets.UTF_8);
    private static final byte[] ROOT_END = "\n</records>".getBytes(StandardCharsets.UTF_8);
    private static final byte[] RECORD_END = "</record>".getBytes(StandardCharsets.UTF_8);
    private int matchIndex = 0; // 检测</record>匹配字节位置, 可能在buffer中被切断

    private final Path pathOutput;
    private OutputStream outputStream;
    private static final int BUFFER_SIZE = 1024 * 1024;

    private final int totalPartNum;
    private int currentPartNum = 1; // 记录当前是第几个切分文件

    private final long totalPartSize;
    private long currentPartSize = 0; // 记录当前文件累加的字节数量

    public FileSplitterBySize(Path pathOutput, int totalPartNum, long totalPartSize) throws IOException {
        this.pathOutput = pathOutput;
        this.totalPartNum = totalPartNum;
        this.totalPartSize = totalPartSize;
        openNewPartition();
    }

    // 创建新的Partition文件并初始化, 第一个拆分文件不需填标签
    private void openNewPartition() throws IOException {
        Path file = pathOutput.resolve("part-" + currentPartNum + ".xml");
        System.out.println("Open output partition file: " + file.getFileName());

        outputStream = new BufferedOutputStream(Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING), BUFFER_SIZE);
        if (currentPartNum > 1) {
            outputStream.write(TITLE);
            outputStream.write(ROOT_START);
        }
        currentPartSize = TITLE.length + ROOT_START.length;
    }

    // 找到完整</record>标签则批量写入, 再判断是否达到拆分文件大小
    public void process(byte[] buffer, int length) throws IOException {
        int offset = 0;
        for (int i = 0; i < length; i++) {
            if (buffer[i] == RECORD_END[matchIndex]) {
                matchIndex++;
                if (matchIndex == RECORD_END.length) {
                    write(buffer, offset, i + 1 - offset);
                    offset = i + 1;
                    matchIndex = 0;
                    if (currentPartNum < totalPartNum && currentPartSize >= totalPartSize) {
                        completePartition();
                        openNewPartition();
                    }
                }
            } else {
                matchIndex = (buffer[i] == RECORD_END[0]) ? 1 : 0;
            }
        }
        // 拷贝最后剩余的字节
        if (offset < length) {
            write(buffer, offset, length - offset);
        }
    }

    // 每写入一个字节就累计PartSize
    private void write(byte[] buffer, int offset, int length) throws IOException {
        outputStream.write(buffer, offset, length);
        currentPartSize += length;
    }

    private void completePartition() throws IOException {
        outputStream.write(ROOT_END);
        outputStream.close();
        currentPartNum++;
    }

    // 确保关闭最后一个拆分文件的输出流
    @Override
    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
    }
}