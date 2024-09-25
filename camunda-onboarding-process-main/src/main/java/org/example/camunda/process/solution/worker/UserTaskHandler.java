package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import java.util.Map;
import org.example.camunda.process.solution.service.MailService;
import org.example.camunda.process.solution.utils.EmailBody;
import org.example.camunda.process.solution.utils.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserTaskHandler {
  @Value("${email.people-and-culture}")
  private String peopleAndCultureEmail;

  @Value("${email.office-operations}")
  private String officeOperationsEmail;

  @Value("${email.support-services}")
  private String supportServicesEmail;

  @Value("${email.ceo}")
  private String ceoEmail;

  private static final Logger LOG = LoggerFactory.getLogger(UserTaskHandler.class);

  private final MailService mailService;

  public UserTaskHandler(MailService mailService) {
    this.mailService = mailService;
  }

  @JobWorker(
      type = "io.camunda.zeebe:userTask",
      timeout = 2592000000L /* 30 days */,
      autoComplete = false,
      fetchVariables = {"processId", "fullName"})
  public void handle(ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String processId = variables.get("processId").toString();
    String fullName = variables.get("fullName").toString();

    String taskName = job.getCustomHeaders().getOrDefault("taskName", "Not Provided");
    String assignee =
        job.getCustomHeaders().getOrDefault("io.camunda.zeebe:assignee", "Not Provided");

    // choose receiver email through assignee
    String receiver;
    switch (assignee) {
      case "people.culture":
        receiver = peopleAndCultureEmail;
        break;
      case "ceo":
        receiver = ceoEmail;
        break;
      case "support.services":
        receiver = supportServicesEmail;
        break;
      case "office.operations":
      default:
        receiver = officeOperationsEmail;
        break;
    }

    LOG.info(String.format("[%s] Sending notification for task `%s`.", processId, taskName));
    mailService.sendMail(
        receiver,
        "New task | " + fullName,
        EmailBody.getUserTaskNotificationBody(
            taskName, fullName, URL.getUserTaskURL(job.getKey())));
    LOG.info(String.format("[%s] Email notification sent.", processId));
  }
}
