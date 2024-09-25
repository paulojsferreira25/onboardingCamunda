package org.example.camunda.process.solution.service;

import java.util.ArrayList;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.example.camunda.process.solution.exception.SpringCamundaException;
import org.example.camunda.process.solution.utils.EmailAttachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailService {
  @Value("${spring.mail.username}")
  String sender;

  private final JavaMailSender mailSender;

  public MailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  @Async
  public void sendMail(String receiver, String subject, String body) throws SpringCamundaException {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();

      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
      messageHelper.setFrom(sender);
      messageHelper.setTo(receiver);
      messageHelper.setSubject(subject);
      messageHelper.setText(body);

      mailSender.send(mimeMessage);

    } catch (MailException | MessagingException e) {
      throw new SpringCamundaException("Exception occurred when sending mail to " + receiver, e);
    }
  }

  @Async
  public void sendMailWithAttachments(
      String receiver, String subject, String body, ArrayList<EmailAttachment> attachments)
      throws SpringCamundaException {
    try {
      MimeMessage mimeMessage = mailSender.createMimeMessage();

      // true flag to indicate multipart message
      MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
      messageHelper.setFrom(sender);
      messageHelper.setTo(receiver);
      messageHelper.setSubject(subject);
      messageHelper.setText(body);

      for (EmailAttachment attachment : attachments) {
        messageHelper.addAttachment(attachment.getName(), attachment.getDataSource());
      }
      ;

      mailSender.send(mimeMessage);

    } catch (MessagingException | MailException e) {
      throw new SpringCamundaException("Exception occurred when sending mail to " + receiver, e);
    }
  }
}
