package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.example.camunda.process.solution.exception.SpringCamundaException;
import org.example.camunda.process.solution.model.CamundaFile;
import org.example.camunda.process.solution.service.CamundaFileService;
import org.example.camunda.process.solution.service.MailService;
import org.example.camunda.process.solution.utils.EmailAttachment;
import org.example.camunda.process.solution.utils.EmailBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailWorker {
  @Value("${email.people-and-culture}")
  private String peopleAndCultureEmail;

  private static final Logger LOG = LoggerFactory.getLogger(MailWorker.class);
  private final MailService mailService;
  private final CamundaFileService camundaFileService;

  public MailWorker(MailService mailService, CamundaFileService camundaFileService) {
    this.mailService = mailService;
    this.camundaFileService = camundaFileService;
  }

  @JobWorker(
      fetchVariables = {
        "processId",
        "fullName",
        "buddyName",
        "companyEmailAddress",
        "buddyEmailAddress"
      })
  public void sendMailsAboutBuddy(final ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String processId = variables.get("processId").toString();
    String fullName = variables.get("fullName").toString();
    String buddyFullName = variables.get("buddyName").toString();
    String companyEmailAddress = variables.get("companyEmailAddress").toString();
    String buddyEmailAddress = variables.get("buddyEmailAddress").toString();

    String firstName = fullName.split(" ")[0];
    String buddyFirstName = buddyFullName.split(" ")[0];

    // send mail to newcomer
    LOG.info(String.format("[%s] Sending buddy-mail to newcomer.", processId));
    this.mailService.sendMail(
        companyEmailAddress,
        "Buddy | " + buddyFullName,
        EmailBody.getBuddyMailToNewcomerBody(firstName, buddyFullName, buddyFirstName));
    LOG.info(String.format("[%s] Buddy-mail sent.", processId));

    // send mail to buddy
    LOG.info(String.format("[%s] Sending buddy system mail to buddy.", processId));
    try {
      // attach buddy system guidelines
      ArrayList<EmailAttachment> attachments = new ArrayList<>();
      byte[] buddySystemGuidelinesContent =
          FileUtils.readFileToByteArray(
              new File("src/main/resources/pdfs/Buddy System @Syone 2022.pdf"));
      attachments.add(
          new EmailAttachment("Buddy System @Syone 2022.pdf", buddySystemGuidelinesContent));

      this.mailService.sendMailWithAttachments(
          buddyEmailAddress,
          "Buddy System | " + fullName,
          EmailBody.getBuddyMailToBuddyBody(fullName, firstName, buddyFirstName),
          attachments);
      LOG.info(String.format("[%s] Buddy System email sent.", processId));
    } catch (Exception e) {
      LOG.error(
          String.format("[%s] Could not send the Buddy System email to the buddy.", processId));
      throw new SpringCamundaException("Could not send the Buddy System email to the buddy.");
    }
  }

  @JobWorker(fetchVariables = {"processId", "emailAddress", "duodecimos"})
  public void sendContractDocsToNewcomer(final ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String processId = variables.get("processId").toString();
    String emailAddress = variables.get("emailAddress").toString();
    String duodecimos = variables.get("duodecimos").toString();

    try {
      ArrayList<EmailAttachment> attachments = new ArrayList<>();
      attachments.add(createAttachment(camundaFileService.getContract(processId))); // add contract
      attachments.add(
          createAttachment(
              camundaFileService.getSITIUsagePolicy(processId))); // add SITI usage policy
      attachments.add(
          createAttachment(camundaFileService.getIHTAgreement(processId))); // add IHT agreement

      // add individual record
      byte[] individualRecordContent =
          FileUtils.readFileToByteArray(
              new File("src/main/resources/pdfs/Ficha Individual de Colaborador.pdf"));
      attachments.add(
          new EmailAttachment("Ficha Individual de Colaborador.pdf", individualRecordContent));

      // add image use authorization
      CamundaFile imageUseAuthorization = camundaFileService.getImageUseAuthorization(processId);
      attachments.add(createAttachment(imageUseAuthorization));

      // add aditamento if necessary
      CamundaFile aditamento = null;
      if (!duodecimos.equals("No")) {
        aditamento = camundaFileService.getAditamento(processId);
        attachments.add(createAttachment(aditamento));
      }

      // send mail
      LOG.info(String.format("[%s] Sending contract documents to newcomer.", processId));
      String aditamentoFilename = (aditamento != null) ? aditamento.getName() : null;
      mailService.sendMailWithAttachments(
          emailAddress,
          "Syone | Contract",
          EmailBody.getSendContractDocsToNewcomerBody(
              aditamentoFilename, imageUseAuthorization.getName(), peopleAndCultureEmail),
          attachments);
      LOG.info(String.format("[%s] Documents sent.", processId));

    } catch (Exception e) {
      LOG.error(String.format("[%s] Could not send contract documents to newcomer.", processId));
      throw new SpringCamundaException("Could not send contract documents to newcomer.");
    }
  }

  private EmailAttachment createAttachment(CamundaFile cf) {
    return new EmailAttachment(cf.getName(), cf.getContent());
  }
}
