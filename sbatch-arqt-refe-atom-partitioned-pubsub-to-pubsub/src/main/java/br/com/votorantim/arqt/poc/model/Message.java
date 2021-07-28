package br.com.votorantim.arqt.poc.model;

import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.pubsub.v1.PubsubMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    private String data;
    private BasicAcknowledgeablePubsubMessage pubsubMessage;

    public Message withData(String data) {
        return this.toBuilder()
                .data(data)
                .build();
    }

    public String getMessageId() {
        return this.pubsubMessage
                .getPubsubMessage()
                .getMessageId();
    }

    public Map<String, String> getAttributesMap() {
        return this.pubsubMessage
                .getPubsubMessage()
                .getAttributesMap();
    }

    public void ack() {
        this.pubsubMessage.ack();
    }

    public void nack() {
        this.pubsubMessage.nack();
    }

    public PubsubMessage getPubsubMessage() {
        return this.pubsubMessage.getPubsubMessage();
    }

}
