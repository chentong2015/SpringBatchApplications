package project.partitions.bycount;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

// 持续从文件中流式读取Buffer数据, 循环处理字节
public class FastXmlCounter {

    private static final int BUFFER_SIZE = 1024 * 1024;
    private static final byte[] START = "<record ".getBytes(StandardCharsets.UTF_8);
    private final Path filepath;

    public FastXmlCounter(Path filepath) {
        this.filepath = filepath;
        if (!Files.isRegularFile(filepath)) {
            throw new RuntimeException("Input file not found: " + filepath);
        }
    }

    public int countRecordsByByte() throws IOException {
        int count = 0;
        TagMatcher matcher = new TagMatcher(START);
        byte[] buffer = new byte[BUFFER_SIZE];
        try (InputStream in = new BufferedInputStream(Files.newInputStream(filepath), BUFFER_SIZE)) {
            int len;
            while ((len = in.read(buffer)) != -1) {
                for (int i = 0; i < len; i++) {
                    if (matcher.match(buffer[i])) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    static class TagMatcher {
        private final byte[] pattern;
        private int matched = 0;

        TagMatcher(byte[] pattern) {
            this.pattern = pattern;
        }

        // 逐个字节一一匹配, 直到完整匹配pattern字节长度，才记录标签
        boolean match(byte b) {
            if (b == pattern[matched]) {
                matched++;
                if (matched == pattern.length) {
                    matched = 0;
                    return true; // 只有match
                }
                return false;
            }
            matched = b == pattern[0] ? 1 : 0;
            return false;
        }
    }
}
