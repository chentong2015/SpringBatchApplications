package project.concurrency_process;

import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.StepContribution;
import org.springframework.batch.core.step.StepExecution;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemStreamReader;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import project.common.bean.DbRecord;
import project.common.bean.Record;
import project.common.RecordItemProcessor;
import project.common.RecordItemReader;
import project.common.RecordItemWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

// TODO. 自定义Tasklet实现Chunk并发执行效果, 包括整个Listener监听器
// - 串行Read: 单线程不断读取数据到内存中(防止OOM)
// - 并行Process+Write: 线程池独立处理数据并存储
@Component
public class ParallelChunkTasklet implements Tasklet {

    private ItemStreamReader<Record> itemReader;
    private RecordItemProcessor itemProcessor;
    private RecordItemWriter itemWriter;

    private final int chunkSize;
    private final Semaphore semaphoreChunkLimiter;
    private final ThreadPoolTaskExecutor taskExecutor;

    public ParallelChunkTasklet(RecordItemReader itemReader,
                                RecordItemProcessor itemProcessor,
                                RecordItemWriter itemWriter,
                                ThreadPoolTaskExecutor taskExecutor) {
        this.itemReader = itemReader;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;
        this.chunkSize = 100; // 每个批次读取的Item数量
        this.taskExecutor = taskExecutor; // 每次线程独立处理一个批次

        // 动态计算能读取多少批次数据到内存, 信号量控制避免OOM内存溢出
        // 批次数量应该大于并发线程数量, 保证最大批次数量的并发效果
        this.semaphoreChunkLimiter = new Semaphore(5);
    }

    // ItemReader Process: open(), read(), close()
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println(chunkContext);
        itemReader.open(contribution.getStepExecution().getExecutionContext());
        try {
            while (true) {
                List<Record> chunkItems = readChunkItems();
                if (chunkItems.isEmpty()) {
                    break; // No more records to process
                }
                semaphoreChunkLimiter.acquire();
                taskExecutor.execute(() -> {
                    try {
                        processAndWriteChunk(chunkItems);
                    } finally {
                        chunkItems.clear();
                        semaphoreChunkLimiter.release();
                    }
                });
            }
            waitForAllTasksCompletion();
        } finally {
            itemReader.close();
        }
        return RepeatStatus.FINISHED;
    }

    // Read chunk size of items for one time
    private List<Record> readChunkItems() throws Exception {
        List<Record> items = new ArrayList<>(chunkSize);
        for (int i = 0; i < chunkSize; i++) {
            Record item = itemReader.read();
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