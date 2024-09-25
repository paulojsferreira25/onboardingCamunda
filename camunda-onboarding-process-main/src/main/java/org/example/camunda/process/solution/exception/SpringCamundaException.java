package org.example.camunda.process.solution.exception;

public class SpringCamundaException extends RuntimeException {
  public SpringCamundaException(String message, Exception exception) {
    super(message, exception);
  }

  public SpringCamundaException(String message) {
    super(message);
  }
}
