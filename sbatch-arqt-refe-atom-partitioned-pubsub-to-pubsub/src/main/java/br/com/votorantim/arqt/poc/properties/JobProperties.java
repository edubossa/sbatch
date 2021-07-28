package br.com.votorantim.arqt.poc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "job")
public class JobProperties {

    private String topic;
    private String subscription;

}
