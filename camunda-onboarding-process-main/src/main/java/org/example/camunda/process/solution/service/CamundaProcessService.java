package org.example.camunda.process.solution.service;

import java.util.Optional;
import org.example.camunda.process.solution.model.CamundaProcess;
import org.example.camunda.process.solution.repository.CamundaProcessRepository;
import org.springframework.stereotype.Service;

@Service
public class CamundaProcessService {
  private final CamundaProcessRepository camundaProcessRepository;

  public CamundaProcessService(CamundaProcessRepository camundaProcessRepository) {
    this.camundaProcessRepository = camundaProcessRepository;
  }

  public CamundaProcess createById(String processId) {
    CamundaProcess camundaProcess = new CamundaProcess(processId);
    camundaProcessRepository.save(camundaProcess);
    return camundaProcess;
  }

  public Optional<CamundaProcess> getById(String processId) {
    return camundaProcessRepository.findById(processId);
  }

  public void deleteById(String processId) {
    camundaProcessRepository.deleteById(processId);
  }
}
