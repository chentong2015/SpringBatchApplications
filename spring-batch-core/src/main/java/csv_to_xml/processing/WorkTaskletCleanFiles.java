package csv_to_xml.processing;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

// TODO. 该Tasklet和Data Processing数据流处理无关
// Tasklet能够完成自定义的准备工作(clean file, call a stored procedure, run SQL update)
public class WorkTaskletCleanFiles implements Tasklet {

    private File directory;

    public WorkTaskletCleanFiles(Resource resource) {
        String errorMessage = "The resource must be a directory";
        try {
            this.directory = resource.getFile();
            Assert.state(this.directory.isDirectory(), errorMessage);
        } catch (IOException exception) {
            System.out.println(errorMessage);
        }
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        File[] files = directory.listFiles();
        for (int i = 0; i < Objects.requireNonNull(files).length; i++) {
            boolean deleted = files[i].delete();
            if (!deleted) {
                System.out.println("Can not delete file");
            }
        }
        return RepeatStatus.FINISHED;
    }
}
