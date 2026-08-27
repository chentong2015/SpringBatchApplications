package project.partitions_largefile.bycount;

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

    private final Path pathInput =  Path.of("drive_folder/xml/records.xml");
    private final Path pathOutput = Path.of("drive_folder/xml/parts");

    @Override
    public @Nullable RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("Start counting xml file...");

        FastXmlCounter xmlCounter = new FastXmlCounter(pathInput);
        int totalCount = xmlCounter.countRecordsByByte();
        int partitionCount = calculatePartitionCount(totalCount);
        System.out.println("Get total count " + totalCount + ", partition count " + partitionCount);

        System.out.println("Start splitting xml file.");
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

    // TODO. 按照统计数量计算每份数量, +1 保证多余数据能够被划分
    private int calculatePartitionCount(int totalCount) {
         if (totalCount < PARTITION_NUM) {
             throw new RuntimeException("Split Failed: total count = " + totalCount + ", PARTITION_NUM = " + PARTITION_NUM);
         }
         return totalCount / PARTITION_NUM + 1;
    }
}
