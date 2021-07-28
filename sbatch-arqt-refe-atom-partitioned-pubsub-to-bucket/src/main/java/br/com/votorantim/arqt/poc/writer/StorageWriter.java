package br.com.votorantim.arqt.poc.writer;

import br.com.votorantim.arqt.poc.model.Message;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StorageWriter implements ItemWriter<Message> {

    private final Storage storage;

    @Value("${gcs-resource-bucket}")
    private String bucket;

    @Override
    public void write(List<? extends Message> messages) throws Exception {
        final List<String> data = messages.stream()
                .map(message -> message.getData() + "\n")
                .collect(Collectors.toList());
        final Blob blob = storage.create(
                BlobInfo.newBuilder(this.bucket, "batch/message-" + UUID.randomUUID().toString() + ".txt").build(),
                data.toString().getBytes()
        );
        log.info("Bucket created with success: " + blob.getGeneratedId());
    }

}
