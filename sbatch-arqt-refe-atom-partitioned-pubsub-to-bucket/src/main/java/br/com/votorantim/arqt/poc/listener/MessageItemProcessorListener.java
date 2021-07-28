package br.com.votorantim.arqt.poc.listener;

import br.com.votorantim.arqt.poc.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;

@Slf4j
public class MessageItemProcessorListener implements ItemProcessListener<Message, Message> {

    @Override
    public void beforeProcess(Message item) {
        log.info("MessageItemProcessor.beforeProcess");
    }

    @Override
    public void afterProcess(Message item, Message result) {
        log.info("Item Process with success!");
        item.ack();
    }

    @Override
    public void onProcessError(Message item, Exception e) {
        log.error("error processing message! " + e.getMessage());
        item.nack();
    }
}
