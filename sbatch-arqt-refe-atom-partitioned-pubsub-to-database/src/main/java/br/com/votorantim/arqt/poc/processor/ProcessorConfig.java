package br.com.votorantim.arqt.poc.processor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProcessorConfig {

    @Bean
    public MessageItemProcessor processor() {
        return new MessageItemProcessor();
    }

}
