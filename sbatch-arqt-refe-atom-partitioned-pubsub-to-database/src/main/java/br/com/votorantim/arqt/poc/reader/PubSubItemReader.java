package br.com.votorantim.arqt.poc.reader;

import br.com.votorantim.arqt.poc.model.Message;
import br.com.votorantim.arqt.poc.properties.JobProperties;
import com.google.cloud.spring.pubsub.core.subscriber.PubSubSubscriberTemplate;
import com.google.cloud.spring.pubsub.support.AcknowledgeablePubsubMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubItemReader implements ItemReader<Message> {

    private final JobProperties properties;
    private final PubSubSubscriberTemplate subscriberTemplate;

    @Override
    public Message read() {
        List<AcknowledgeablePubsubMessage> messages = this.subscriberTemplate
                                        .pull(this.properties.getSubscription(), 1, true);
        if (messages.isEmpty()) return null;
        final var pubsubMessage = messages.get(0);
        final var message = new Message(pubsubMessage.getPubsubMessage().getData()
                .toStringUtf8(), pubsubMessage);
        log.info("reading message from pubsub: " + message.getMessageId());
        return message;
    }

}
