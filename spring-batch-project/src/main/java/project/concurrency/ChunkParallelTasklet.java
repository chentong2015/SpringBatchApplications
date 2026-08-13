package project.concurrency;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import project.base_bean.DbRecord;
import project.base_bean.Record;
import project.base_process.RecordItemProcessor;
import project.base_process.RecordItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// TODO. 自定义Tasklet并发执行
// - 串行Read: 单线程不断读取数据到内存中(防止OOM)
// - 并行Process+Write: 线程池独立处理数据并存储
@Component
public class ChunkParallelTasklet implements Tasklet {

    // 可以为工作流对应的Listener
    private ItemStreamReader<Record> itemStreamReader;
    private RecordItemProcessor itemProcessor;
    private RecordItemWriter itemWriter;

    private final int chunkSize;
    private final int chunkCountLimit;
    private final Semaphore semaphore;
    private final ThreadPoolTaskExecutor taskExecutor;

    public ChunkParallelTasklet(ItemStreamReader<Record> itemStreamReader,
                                RecordItemProcessor itemProcessor,
                                RecordItemWriter itemWriter,
                                ThreadPoolTaskExecutor taskExecutor) {
        this.itemStreamReader = itemStreamReader;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;

        this.chunkSize = 100;
        this.chunkCountLimit = 5; // 动态计算能读取多少批次数据到内存(>=并发线程数量)
        this.semaphore = new Semaphore(chunkCountLimit); // 通过信号量控制读取批次数量
        this.taskExecutor = taskExecutor; // 每次线程独立处理一个批次
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        ExecutionContext executionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
        itemStreamReader.open(executionContext);
        try {
            while (true) {
                List<Record> chunkItems = readChunkItems();
                if (chunkItems.isEmpty()) {
                    break; // No more records to process
                }
                semaphore.acquire();
                taskExecutor.execute(() -> {
                    try {
                        processAndWriteChunk(chunkItems);
                    } finally {
                        chunkItems.clear();
                        semaphore.release();
                    }
                });
            }
            waitForAllTasksCompletion();
        } finally {
            itemStreamReader.close();
        }
        return RepeatStatus.FINISHED;
    }

    // Read chunk size of items for one time
    private List<Record> readChunkItems() throws Exception {
        List<Record> items = new ArrayList<>(chunkSize);
        for (int i = 0; i < chunkSize; i++) {
            Record item = itemStreamReader.read();
            if (item == null) {
                break; // End of XML File
            }
            items.add(item);
        }
        return items;
    }

    private void processAndWriteChunk(List<Record> chunkItems) {
        try {
            List<DbRecord> processedItems = new ArrayList<>(chunkItems.size());
            for (Record item : chunkItems) {
                DbRecord result = itemProcessor.process(item);
                if (result != null) {
                    processedItems.add(result);
                }
            }
            if (!processedItems.isEmpty()) {
                Chunk<DbRecord> chunkResult = new Chunk<>(processedItems);
                itemWriter.write(chunkResult);
            }
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    private void waitForAllTasksCompletion() {
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        threadPoolExecutor.shutdown();
        while (!threadPoolExecutor.isTerminated()) {
            try {
                threadPoolExecutor.awaitTermination(3, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}