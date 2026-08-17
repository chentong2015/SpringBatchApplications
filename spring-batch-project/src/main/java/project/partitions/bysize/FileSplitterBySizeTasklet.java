package project.partitions.bysize;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// TODO. 按照文件太小拆分可能不均匀, 且小文件无法拆分成指定多的份数(造成空文件)
@Component
public class FileSplitterBySizeTasklet implements Tasklet {

    private static final int PARTITION_NUM = 4; // 切分文件的份数/并发线程数
    private static final int BUFFER_SIZE = 1024 * 1024; // 1MB 缓存到内存的大小

    private final Path pathInput = Path.of("drive_folder/xml/records.xml");
    private final Path pathOutput = Path.of("drive_folder/xml/parts");

    // 持续从文件中流式读取Buffer数据, 避免撑爆内存
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Start splitting xml file...");
        long partitionSize = Files.size(pathInput) / PARTITION_NUM;

        FileSplitterBySize splitter = new FileSplitterBySize(pathOutput, PARTITION_NUM, partitionSize);
        try (InputStream in = new BufferedInputStream(Files.newInputStream(pathInput), BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                splitter.process(buffer, len);
            }
        }
        splitter.close();

        System.out.println("Finish splitting xml file.");
        return RepeatStatus.FINISHED;
    }
}