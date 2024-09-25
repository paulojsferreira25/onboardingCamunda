package org.example.camunda.process.solution.controller;

import java.util.Map;
import org.example.camunda.process.solution.model.FormAccessKey;
import org.example.camunda.process.solution.service.FormAccessKeyService;
import org.example.camunda.process.solution.service.MailService;
import org.example.camunda.process.solution.utils.EmailBody;
import org.example.camunda.process.solution.utils.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(
    origins = {"*"},
    maxAge = 4800,
    allowCredentials = "false")
@RestController
@RequestMapping("/mail")
public class MailController {
  private static final Logger LOG = LoggerFactory.getLogger(MailController.class);
  private final FormAccessKeyService formAccessKeyService;
  private final MailService mailService;

  public MailController(FormAccessKeyService formAccessKeyService, MailService mailService) {
    this.formAccessKeyService = formAccessKeyService;
    this.mailService = mailService;
  }

  @PostMapping("/send-onboarding-form")
  public ResponseEntity<?> sendOnboardingFormToNewcomer(@RequestBody Map<String, String> body) {
    try {
      String firstName = body.get("firstName").toString();
      String emailAddress = body.get("emailAddress").toString();

      FormAccessKey formAccessKey = formAccessKeyService.generate();

      LOG.info("Sending Newcomer Onboarding Form to " + emailAddress + ".");
      mailService.sendMail(
          emailAddress,
          "Syone | Next Steps",
          EmailBody.getSendOnboardingFormToNewcomerBody(
              firstName, URL.getOnboardingFormURL(formAccessKey.getAccessKey())));
      LOG.info("Form sent.");
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return ResponseEntity.internalServerError().build();
    }
  }
}
