package org.example.camunda.process.solution.controller;

import io.camunda.zeebe.client.ZeebeClient;
import java.io.IOException;
import java.util.UUID;
import org.example.camunda.process.solution.ProcessConstants;
import org.example.camunda.process.solution.dto.NewcomerDTO;
import org.example.camunda.process.solution.enums.CamundaFileType;
import org.example.camunda.process.solution.service.CamundaFileService;
import org.example.camunda.process.solution.service.CamundaProcessService;
import org.example.camunda.process.solution.service.FormAccessKeyService;
import org.example.camunda.process.solution.utils.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
    origins = {"*"},
    maxAge = 4800,
    allowCredentials = "false")
@RestController
@RequestMapping("/process")
public class ProcessController {
  private static final Logger LOG = LoggerFactory.getLogger(ProcessController.class);
  private final ZeebeClient zeebe;
  private final FormAccessKeyService formAccessKeyService;
  private final CamundaProcessService camundaProcessService;
  private final CamundaFileService camundaFileService;

  public ProcessController(
      ZeebeClient client,
      FormAccessKeyService formAccessKeyService,
      CamundaProcessService camundaProcessService,
      CamundaFileService camundaFileService) {
    this.zeebe = client;
    this.formAccessKeyService = formAccessKeyService;
    this.camundaProcessService = camundaProcessService;
    this.camundaFileService = camundaFileService;
  }

  @PostMapping("/start")
  public ResponseEntity<?> startProcessInstance(
      @RequestPart String accessKey,
      @RequestPart NewcomerDTO newcomerInfo,
      @RequestPart(required = false) MultipartFile photo,
      @RequestPart(required = false) MultipartFile certificateOfQualifications) {
    String processId = Long.toString(UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE);
    newcomerInfo.setProcessId(processId);
    newcomerInfo.setContractLink(URL.getContractURL(processId));
    newcomerInfo.setUploadContractFormLink(URL.getUploadContractFormURL(processId));

    try {
      // store process in db
      camundaProcessService.createById(processId);

      // store photo in db
      if (photo != null && !photo.isEmpty()) {
        try {
          LOG.info(String.format("[%s] Storing photo in database.", processId));
          camundaFileService.store(
              processId, photo.getOriginalFilename(), CamundaFileType.PHOTO, photo.getBytes());
          newcomerInfo.setPhotoLink(URL.getPhotoURL(processId));
          LOG.info(String.format("[%s] Photo Stored.", processId));
        } catch (IOException e) {
          LOG.error(String.format("[%s] Failed to store photo.", processId));
        }
      }

      // store certificate in db
      if (certificateOfQualifications != null && !certificateOfQualifications.isEmpty()) {
        try {
          LOG.info(
              String.format("[%s] Storing certificate of qualifications in database.", processId));
          camundaFileService.store(
              processId,
              certificateOfQualifications.getOriginalFilename(),
              CamundaFileType.CERTIFICATE_OF_QUALIFICATIONS,
              certificateOfQualifications.getBytes());
          newcomerInfo.setCertificateOfQualificationsLink(
              URL.getCertificateOfQualificationsURL(processId));
          LOG.info(String.format("[%s] Certificate of qualifications Stored.", processId));
        } catch (IOException e) {
          LOG.error(
              String.format("[%s] Failed to store certificate of qualifications.", processId));
        }
      }

      // start process
      LOG.info(
          String.format(
              "[%s] Starting process `%s` with variables: %s",
              processId, ProcessConstants.BPMN_PROCESS_ID, newcomerInfo));
      zeebe
          .newCreateInstanceCommand()
          .bpmnProcessId(ProcessConstants.BPMN_PROCESS_ID)
          .latestVersion()
          .variables(newcomerInfo)
          .send();

      // delete form access key
      LOG.info(
          String.format("[%s] Deleting form access key (%s) from database.", processId, accessKey));
      formAccessKeyService.deleteKey(accessKey);
      LOG.info(String.format("[%s] Form access key deleted.", processId));

      return ResponseEntity.ok().build();

    } catch (Exception e) {
      camundaProcessService.deleteById(processId);
      return ResponseEntity.internalServerError().build();
    }
  }
}
