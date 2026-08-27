package project.concurrency_read.KO;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

// TODO. 在不拆分文件的条件下: 根据Tag数量来划分文件不同Index区域
public class RangeXmlItemPartitionerKO implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>();
        long totalRecords = 6; // call count method
        long recordsPerPartition = totalRecords / gridSize;
        long remainder = totalRecords % gridSize;

        long start = 0;
        for (int i = 0; i < gridSize; i++) {
            long size = recordsPerPartition + (i < remainder ? 1 : 0);
            if (size == 0) {
                // Do not generate extra ExecutionContext when there is not enough records
                continue;
            }
            long end = start + size;
            ExecutionContext context = new ExecutionContext();
            context.putLong("startIndex", start);
            context.putLong("endIndex", end);
            partitions.put("partition-" + i, context);
            start = end;
        }
        return partitions;
    }

    // Total=6, Grid=8
    // partition-0={startIndex=0, endIndex=1}
    // partition-1={startIndex=1, endIndex=2}
    // partition-2={startIndex=2, endIndex=3}
    // partition-3={startIndex=3, endIndex=4}
    // partition-4={startIndex=4, endIndex=5}
    // partition-5={startIndex=5, endIndex=6}
    //
    // Total=1100, Grid=8
    // partition-0={startIndex=0, endIndex=138}
    // partition-1={startIndex=138, endIndex=276}
    // partition-2={startIndex=276, endIndex=414}
    // partition-3={startIndex=414, endIndex=552}
    // partition-4={startIndex=552, endIndex=689}
    // partition-5={startIndex=689, endIndex=826}
    // partition-6={startIndex=826, endIndex=963}
    // partition-7={startIndex=963, endIndex=1100}
    public static void main(String[] args) {
        RangeXmlItemPartitionerKO instance = new RangeXmlItemPartitionerKO();
        Map<String, ExecutionContext> mapPartitions = instance.partition(8);
        System.out.println(mapPartitions);
    }
}
