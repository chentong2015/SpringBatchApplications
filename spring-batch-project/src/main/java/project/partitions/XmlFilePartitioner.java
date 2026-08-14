package project.partitions;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

// TODO. 为Partition Step的执行创建对应的Context
// Partitioner to be used to construct new step executions
// StepExecution
//      └── ExecutionContext
//              └── file = /data/parts/part-0.xml
@Component
public class XmlFilePartitioner implements Partitioner {

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> stepExecution = new HashMap<>();
        for (int i = 0; i < gridSize; i++) {
            ExecutionContext context = new ExecutionContext();
            context.putString("file", "drive_folder/xml/parts/part-" + i + ".xml");
            stepExecution.put("partition-" + i, context);
        }
        return stepExecution;
    }
}
