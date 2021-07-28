package br.com.votorantim.arqt.poc.partitioner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.deployer.resource.support.DelegatingResourceLoader;
import org.springframework.cloud.deployer.spi.task.TaskLauncher;
import org.springframework.cloud.task.batch.partition.DeployerPartitionHandler;
import org.springframework.cloud.task.batch.partition.DeployerStepExecutionHandler;
import org.springframework.cloud.task.batch.partition.PassThroughCommandLineArgsProvider;
import org.springframework.cloud.task.batch.partition.SimpleEnvironmentVariablesProvider;
import org.springframework.cloud.task.repository.TaskRepository;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Ref:
 *  https://docs.spring.io/spring-cloud-task/docs/current/reference/html/#batch-partitioning
 *  https://github.com/spring-cloud/spring-cloud-task/tree/main/spring-cloud-task-samples/partitioned-batch-job
 *  https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/scalability.html#partitioning
 *  https://docs.spring.io/spring-batch/docs/4.3.x/reference/html/spring-batch-integration.html#remote-partitioning
 *  https://dataflow.spring.io/docs/feature-guides/batch/partitioning/
 */
@Configuration
@Slf4j
@RequiredArgsConstructor
public class PartitionHandlerConfig {

    private final DelegatingResourceLoader resourceLoader;
    private final ConfigurableApplicationContext context;
    public final JobRepository jobRepository;
    private final Environment environment;

    @Value("${particionamento.image}")
    private String image;

    @Bean
    public PartitionHandler partitionHandler(TaskLauncher taskLauncher, JobExplorer jobExplorer, TaskRepository taskRepository) throws Exception {
        Resource resource = this.resourceLoader.getResource(image);
        log.info("Starting partitionHandler! " + this.image);
        DeployerPartitionHandler partitionHandler = new DeployerPartitionHandler(taskLauncher, jobExplorer, resource, "workerStep", taskRepository);
        List<String> commandLineArgs = new ArrayList<>(3);
        commandLineArgs.add("--spring.profiles.active=worker");
        commandLineArgs.add("--spring.cloud.task.initialize-enabled=false");
        commandLineArgs.add("--spring.batch.initializer.enabled=false");
        partitionHandler.setCommandLineArgsProvider(new PassThroughCommandLineArgsProvider(commandLineArgs));
        partitionHandler.setEnvironmentVariablesProvider(new SimpleEnvironmentVariablesProvider(this.environment));
        partitionHandler.setMaxWorkers(1);
        partitionHandler.setApplicationName("PartitionedBatchJobTask");
        return partitionHandler;
    }

    @Bean
    @Profile("worker")
    public DeployerStepExecutionHandler stepExecutionHandler(JobExplorer jobExplorer) {
        return new DeployerStepExecutionHandler(this.context, jobExplorer, this.jobRepository);
    }

}
