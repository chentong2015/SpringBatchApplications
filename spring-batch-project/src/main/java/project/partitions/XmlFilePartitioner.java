package project.partitions;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// TODO. 为PartitionStep创建执行上下文, Step通过参数值找到操作文件
// Partitioner to be used to construct new step executions
// StepExecution
//      └── ExecutionContext
//              └── part-file = /data/parts/part-1.xml
//              └── part-file = /data/parts/part-2.xml
//              └── part-file = /data/parts/part-3.xml
@Component
public class XmlFilePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> stepExecution = new HashMap<>();
        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();
            context.putString("part-file", "drive_folder/xml/parts/part-" + (i+1) + ".xml");
            stepExecution.put("partition-" + (i+1), context);
        }
        return stepExecution;
    }
}
