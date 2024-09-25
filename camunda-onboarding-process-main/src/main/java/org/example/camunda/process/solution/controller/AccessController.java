package org.example.camunda.process.solution.controller;

import java.util.HashMap;
import java.util.Map;
import org.example.camunda.process.solution.service.FormAccessKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
    origins = {"*"},
    maxAge = 4800,
    allowCredentials = "false")
@RestController
@RequestMapping("/access")
public class AccessController {
  private FormAccessKeyService formAccessKeyService;

  public AccessController(FormAccessKeyService formAccessKeyService) {
    this.formAccessKeyService = formAccessKeyService;
  }

  @GetMapping("/validate-key/{key}")
  public ResponseEntity<Map<String, Boolean>> validateAccessKey(@PathVariable String key) {
    Map<String, Boolean> map = new HashMap<>();
    map.put("isValid", formAccessKeyService.validateKey(key));
    return ResponseEntity.ok().body(map);
  }
}
