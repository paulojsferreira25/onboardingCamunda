package org.example.camunda.process.solution.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class FormAccessKey {
  @Id private String accessKey;

  public FormAccessKey() {}

  public FormAccessKey(String accessKey) {
    this.accessKey = accessKey;
  }
}
