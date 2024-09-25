package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.spring.client.annotation.JobWorker;
import io.camunda.zeebe.spring.client.annotation.Variable;
import org.example.camunda.process.solution.service.CamundaProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ProcessWorker {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessWorker.class);
  private final CamundaProcessService camundaProcessService;

  public ProcessWorker(CamundaProcessService camundaProcessService) {
    this.camundaProcessService = camundaProcessService;
  }

  @JobWorker
  public void performProcessCleanup(@Variable String processId) {
    LOG.info(String.format("[%s] Deleting all process files from database.", processId));
    camundaProcessService.deleteById(processId);
    LOG.info(String.format("[%s] Files deleted.", processId));
  }
}
