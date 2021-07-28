package br.com.votorantim.arqt.poc.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PubSubJobListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        log.info("PubSubJobListener.beforeJob");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("PubSubJobCompletionNotificationListener.afterJob");
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED! Time to verify the results");
            log.warn("JOB_INSTANCE_ID: " + jobExecution.getJobId());
            log.warn("JOB STATUS: " + jobExecution.getStatus().name());
        } else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            log.error("jobExecution job failure: " + jobExecution.getStatus().name());
        }
    }
}
