package project.partitions.bycount;

import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class FileSplitterByCountTasklet implements Tasklet {

    private static final int PARTITION_NUM = 8;
    private static final int BUFFER_SIZE = 1024 * 1024;

    private final Path pathInput =  Path.of("drive_folder/xml/F_2026_07_09_world-check.xml/F_2026_07_09_world-check.xml");
    private final Path pathOutput = Path.of("drive_folder/xml/full");

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Start splitting xml file...");
        FastXmlCounter xmlCounter = new FastXmlCounter(pathInput);
        int totalCount = xmlCounter.countRecordsByByte();
        int partitionCount = calculatePartitionCount(totalCount);
        System.out.println("Get total count " + totalCount + ", partition count " + partitionCount);

        FileSplitterByCount splitterByCount = new FileSplitterByCount(pathOutput, PARTITION_NUM, partitionCount);
        try (InputStream in = new BufferedInputStream(Files.newInputStream(pathInput), BUFFER_SIZE)) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int len;
            while ((len = in.read(buffer)) != -1) {
                splitterByCount.process(buffer, len);
            }
        }
        splitterByCount.close();

        System.out.println("Finish splitting xml file.");
        return RepeatStatus.FINISHED;
    }

    // TODO. 按照统计数量来计算切分数据, +1保证多余数据能够被划分
    private int calculatePartitionCount(int totalCount) {
         if (totalCount < PARTITION_NUM) {
             throw new RuntimeException("Can not split to num of partitions: " + PARTITION_NUM);
         }
         return totalCount / PARTITION_NUM + 1;
    }
}
