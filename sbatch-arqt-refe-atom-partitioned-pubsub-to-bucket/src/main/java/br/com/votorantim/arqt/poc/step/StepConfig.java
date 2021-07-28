package br.com.votorantim.arqt.poc.step;

import br.com.votorantim.arqt.poc.listener.MessageItemProcessorListener;
import br.com.votorantim.arqt.poc.model.Message;
import br.com.votorantim.arqt.poc.partitioner.PubSubPartitioner;
import br.com.votorantim.arqt.poc.processor.MessageItemProcessor;
import br.com.votorantim.arqt.poc.reader.PubSubItemReader;
import br.com.votorantim.arqt.poc.writer.StorageWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class StepConfig {

    private final StepBuilderFactory stepBuilderFactory;
    private final PubSubItemReader pubSubItemReader;
    private final StorageWriter storageWriter;
    private final MessageItemProcessor processor;
    private final PubSubPartitioner partitioner;
    private final PartitionHandler partitionHandler;

    @Bean
    public Step splitWorkers() {
        return this.stepBuilderFactory.get("splitWorkers")
                .partitioner(workerStep().getName(), this.partitioner)
                .step(workerStep())
                .partitionHandler(this.partitionHandler)
                .build();
    }

    @Bean
    public Step workerStep() {
        return this.stepBuilderFactory.get("workerStep")
                .allowStartIfComplete(Boolean.TRUE)
                .<Message, Message>chunk(1000)
                .reader(this.pubSubItemReader)
                .processor(this.processor)
                .listener(new MessageItemProcessorListener())
                .writer(this.storageWriter)
                .build();
    }

}
