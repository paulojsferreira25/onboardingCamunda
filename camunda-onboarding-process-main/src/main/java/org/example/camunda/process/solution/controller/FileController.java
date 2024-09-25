package org.example.camunda.process.solution.controller;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.example.camunda.process.solution.enums.CamundaFileType;
import org.example.camunda.process.solution.model.CamundaFile;
import org.example.camunda.process.solution.service.CamundaFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(
    origins = {"*"},
    maxAge = 4800,
    allowCredentials = "false")
@RestController
@RequestMapping("/file")
public class FileController {
  private static final Logger LOG = LoggerFactory.getLogger(FileController.class);
  private final CamundaFileService camundaFileService;

  public FileController(CamundaFileService camundaFileService) {
    this.camundaFileService = camundaFileService;
  }

  @GetMapping("/syone-logo")
  public ResponseEntity<byte[]> getCompanyLogo() {
    try {
      byte[] logo =
          FileUtils.readFileToByteArray(new File("src/main/resources/images/syone-logo.jpg"));
      return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(logo);
    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/company-signature")
  public ResponseEntity<byte[]> getCompanySignature() {
    try {
      byte[] signature =
          FileUtils.readFileToByteArray(
              new File("src/main/resources/images/company-signature.png"));
      return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(signature);
    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/individual-record")
  public ResponseEntity<byte[]> getIndividualRecord() {
    try {
      byte[] content =
          FileUtils.readFileToByteArray(
              new File("src/main/resources/pdfs/Ficha Individual de Colaborador.pdf"));
      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION,
              "inline; filename=\"Ficha Individual de Colaborador.pdf\"")
          .contentType(MediaType.APPLICATION_PDF)
          .body(content);
    } catch (IOException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping("/upload/contract")
  public ResponseEntity<?> uploadContract(
      @RequestPart String processId, @RequestPart MultipartFile contract) {
    try {
      if (!contract.isEmpty()) {
        LOG.info(String.format("[%s] Storing uploaded contract in database.", processId));
        camundaFileService.store(
            processId,
            contract.getOriginalFilename(),
            CamundaFileType.CONTRACT,
            contract.getBytes());
        LOG.info(String.format("[%s] Contract Stored.", processId));
      } else {
        LOG.info(String.format("[%s] Uploaded contract is empty.", processId));
      }
      return ResponseEntity.ok().build();
    } catch (IOException e) {
      LOG.error(String.format("[%s] Failed to store contract.", processId));
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{processId}/{fileType}")
  public ResponseEntity<byte[]> getFileFromDB(
      @PathVariable String processId, @PathVariable String fileType) {
    try {
      CamundaFile camundaFile =
          camundaFileService.getFile(processId, CamundaFileType.fromType(fileType));

      MediaType mediaType;
      if (fileType.equals(CamundaFileType.PHOTO.getType())) {
        mediaType = MediaType.IMAGE_JPEG;
      } else {
        mediaType = MediaType.APPLICATION_PDF;
      }

      return ResponseEntity.ok()
          .header(
              HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + camundaFile.getName() + "\"")
          .contentType(mediaType)
          .body(camundaFile.getContent());
    } catch (NullPointerException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
