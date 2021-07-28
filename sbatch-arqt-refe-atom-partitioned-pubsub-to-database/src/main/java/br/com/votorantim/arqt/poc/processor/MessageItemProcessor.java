package br.com.votorantim.arqt.poc.processor;

import br.com.votorantim.arqt.poc.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class MessageItemProcessor implements ItemProcessor<Message, Message> {

    @Autowired
    private ApplicationArguments applicationArguments;

    @Override
    public Message process(final Message message) throws Exception {
        final var transformMessage = message.getData().toUpperCase();
        log.info("Converting (" + message + ") into: (" + transformMessage + ")");
        checkErrorSimulated();
        return message.withData(transformMessage);
    }

    private void checkErrorSimulated() {
        final Optional<String> error = Arrays
                .stream(this.applicationArguments.getSourceArgs())
                .filter(arg -> arg.equalsIgnoreCase("error"))
                .findAny();
        if (error.isPresent()) {
            throw new RuntimeException("FORCANDO ERROR!");
        }
    }

}
