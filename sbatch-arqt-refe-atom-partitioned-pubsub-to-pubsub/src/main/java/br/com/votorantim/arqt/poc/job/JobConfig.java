package br.com.votorantim.arqt.poc.job;

import br.com.votorantim.arqt.poc.listener.PubSubJobListener;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class JobConfig {

    private final JobBuilderFactory jobBuilderFactory;

    @Bean
    @Profile("!worker")
    public Job pocPubsub(PubSubJobListener listener, Step splitWorkers) {
        return jobBuilderFactory.get("partitionedJobPubSubToPubSub")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(splitWorkers)
                .build();
    }


}
