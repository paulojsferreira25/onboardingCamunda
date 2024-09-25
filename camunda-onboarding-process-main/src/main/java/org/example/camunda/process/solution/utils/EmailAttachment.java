package org.example.camunda.process.solution.utils;

import javax.mail.util.ByteArrayDataSource;

public class EmailAttachment {
  private String name;
  private ByteArrayDataSource dataSource;

  public EmailAttachment(String name, byte[] content) {
    this.name = name;
    this.dataSource = new ByteArrayDataSource(content, "application/octet-stream");
  }

  public String getName() {
    return this.name;
  }

  public ByteArrayDataSource getDataSource() {
    return this.dataSource;
  }
}
