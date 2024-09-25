package org.example.camunda.process.solution.service;

import java.util.UUID;
import org.example.camunda.process.solution.model.FormAccessKey;
import org.example.camunda.process.solution.repository.FormAccessKeyRepository;
import org.springframework.stereotype.Service;

@Service
public class FormAccessKeyService {
  private final FormAccessKeyRepository formAccessKeyRepository;

  public FormAccessKeyService(FormAccessKeyRepository formAccessKeyRepository) {
    this.formAccessKeyRepository = formAccessKeyRepository;
  }

  public FormAccessKey generate() {
    String key = UUID.randomUUID().toString();
    FormAccessKey formAccessKey = new FormAccessKey(key);
    formAccessKeyRepository.save(formAccessKey);
    return formAccessKey;
  }

  public void deleteKey(String key) {
    formAccessKeyRepository.deleteById(key);
  }

  public boolean validateKey(String key) {
    if (formAccessKeyRepository.findById(key).isPresent()) return true;
    return false;
  }
}
