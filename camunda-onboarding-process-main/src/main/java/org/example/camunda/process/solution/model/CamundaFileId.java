package org.example.camunda.process.solution.model;

import java.io.Serializable;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Embeddable
public class CamundaFileId implements Serializable {
  private String type;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "process_id", nullable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private CamundaProcess process;
}
