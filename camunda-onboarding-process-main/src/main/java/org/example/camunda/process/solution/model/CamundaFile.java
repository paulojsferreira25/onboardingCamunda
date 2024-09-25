package org.example.camunda.process.solution.model;

import javax.persistence.*;

@Entity
public class CamundaFile {
  @EmbeddedId private CamundaFileId id;
  private String name;
  @Lob private byte[] content;

  public CamundaFile() {
    this.id = new CamundaFileId();
  }

  public CamundaFile(String type, CamundaProcess process, String name, byte[] content) {
    this.id = new CamundaFileId();
    this.id.setType(type);
    this.id.setProcess(process);
    this.name = name;
    this.content = content;
  }

  public String getType() {
    return this.id.getType();
  }

  public void setType(String type) {
    this.id.setType(type);
  }

  public CamundaProcess getProcess() {
    return this.id.getProcess();
  }

  public void setProcess(CamundaProcess process) {
    this.id.setProcess(process);
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getContent() {
    return this.content;
  }

  public void setContent(byte[] content) {
    this.content = content;
  }
}
