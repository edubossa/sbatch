package br.com.votorantim.arqt.poc;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.task.configuration.EnableTask;

// export GOOGLE_APPLICATION_CREDENTIALS=/Users/wallace/app/service-account.json
@EnableTask
@EnableBatchProcessing
@SpringBootApplication
@ConfigurationPropertiesScan("br.com.votorantim.arqt.poc.properties")
public class BatchApplication {

	public static void main(String[] args) {
		System.exit(
				SpringApplication.exit(
						SpringApplication.run(BatchApplication.class, args)
				)
		);
	}

}
