package br.com.votorantim.arqt.poc.partitioner;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PubSubPartitioner implements Partitioner {

    private static final int GRID_SIZE = 3;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> partitions = new HashMap<>(gridSize);

        for (int i = 0; i < GRID_SIZE; i++) {
            ExecutionContext context1 = new ExecutionContext();
            context1.put("partitionNumber", i);

            partitions.put("partition" + i, context1);
        }

        return partitions;
    }

}
