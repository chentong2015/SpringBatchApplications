package project.partitions_paging;

import org.springframework.batch.core.partition.Partitioner;
import org.springframework.batch.infrastructure.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RangedIndexPartitioner implements Partitioner {

    // 每个Slave Step独立处理1000个Id的数据
    private static final int INDEX_RANGED = 1000;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new HashMap<>();
        int fromId = 1;
        int toId = INDEX_RANGED;

        for (int i = 1; i <= gridSize; i++) {
            ExecutionContext executionContext = new ExecutionContext();
            executionContext.putInt("fromId", fromId);
            executionContext.putInt("toId", toId);
            result.put("partition" + i, executionContext);
            fromId = toId + 1;
            toId += INDEX_RANGED;
        }
        System.out.println(result);
        return result;
    }
}
