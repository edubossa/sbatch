package br.com.votorantim.arqt.poc.writer;


import br.com.votorantim.arqt.poc.model.Message;
import br.com.votorantim.arqt.poc.properties.JobProperties;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubItemWriter implements ItemWriter<Message> {

    private final JobProperties properties;
    private final PubSubPublisherTemplate publisher;

    @Override
    public void write(List<? extends Message> messages) throws Exception {
        log.info("write total messages chunk: " + messages.size());
        messages.forEach(message -> {
            ListenableFuture<String> publish = this.publisher.publish(this.properties.getTopic(), message.getData());
            publish.addCallback(success -> log.info("PubSubItemWriter to batch-xml-process with successful " + success),
                         error -> log.error(error.getMessage(), error)
            );
            try {
                publish.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

}
