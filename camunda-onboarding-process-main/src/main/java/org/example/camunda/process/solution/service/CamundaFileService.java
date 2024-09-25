package org.example.camunda.process.solution.service;

import org.example.camunda.process.solution.enums.CamundaFileType;
import org.example.camunda.process.solution.model.CamundaFile;
import org.example.camunda.process.solution.repository.CamundaFileRepository;
import org.springframework.stereotype.Service;

@Service
public class CamundaFileService {
  private final CamundaFileRepository camundaFileRepository;
  private final CamundaProcessService camundaProcessService;

  public CamundaFileService(
      CamundaFileRepository camundaFileRepository, CamundaProcessService camundaProcessService) {
    this.camundaFileRepository = camundaFileRepository;
    this.camundaProcessService = camundaProcessService;
  }

  public void store(String processId, String name, CamundaFileType type, byte[] content) {
    camundaProcessService
        .getById(processId)
        .ifPresent(
            process -> {
              CamundaFile camundaFile = new CamundaFile(type.getType(), process, name, content);
              camundaFileRepository.save(camundaFile);
            });
  }

  public CamundaFile getFile(String processId, CamundaFileType type) {
    return camundaFileRepository.findByProcessIdAndType(processId, type.getType());
  }

  public CamundaFile getContract(String processId) {
    return camundaFileRepository.findByProcessIdAndType(
        processId, CamundaFileType.CONTRACT.getType());
  }

  public CamundaFile getSITIUsagePolicy(String processId) {
    return camundaFileRepository.findByProcessIdAndType(
        processId, CamundaFileType.SITI_USAGE_POLICY.getType());
  }

  public CamundaFile getIHTAgreement(String processId) {
    return camundaFileRepository.findByProcessIdAndType(
        processId, CamundaFileType.IHT_AGREEMENT.getType());
  }

  public CamundaFile getAditamento(String processId) {
    return camundaFileRepository.findByProcessIdAndType(
        processId, CamundaFileType.ADITAMENTO.getType());
  }

  public CamundaFile getImageUseAuthorization(String processId) {
    return camundaFileRepository.findByProcessIdAndType(
        processId, CamundaFileType.IMAGE_USE_AUTHORIZATION.getType());
  }
}
