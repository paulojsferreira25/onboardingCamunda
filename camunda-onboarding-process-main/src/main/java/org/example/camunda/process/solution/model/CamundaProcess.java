package org.example.camunda.process.solution.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CamundaProcess {
  @Id String processId;

  public CamundaProcess() {}

  public CamundaProcess(String processId) {
    this.processId = processId;
  }
}
