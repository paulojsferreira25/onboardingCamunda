package org.example.camunda.process.solution.utils;

public class URL {
  private static final String host = "http://localhost";
  private static final String backendPort = "8080";
  private static final String frontendPort = "3000";
  private static final String tasklistPort = "8082";

  public static String getPhotoURL(String processId) {
    return String.format("%s:%s/file/%s/photo", host, backendPort, processId);
  }

  public static String getCertificateOfQualificationsURL(String processId) {
    return String.format(
        "%s:%s/file/%s/certificate-of-qualifications", host, backendPort, processId);
  }

  public static String getContractURL(String processId) {
    return String.format("%s:%s/file/%s/contract", host, backendPort, processId);
  }

  public static String getSITIUsagePolicyURL(String processId) {
    return String.format("%s:%s/file/%s/siti-usage-policy", host, backendPort, processId);
  }

  public static String getIHTAgreementURL(String processId) {
    return String.format("%s:%s/file/%s/iht-agreement", host, backendPort, processId);
  }

  public static String getAditamentoURL(String processId) {
    return String.format("%s:%s/file/%s/aditamento", host, backendPort, processId);
  }

  public static String getIndividualRecordURL() {
    return String.format("%s:%s/file/individual-record", host, backendPort);
  }

  public static String getImageUseAuthorizationURL(String processId) {
    return String.format("%s:%s/file/%s/image-use-authorization", host, backendPort, processId);
  }

  public static String getUserTaskURL(long jobKey) {
    return String.format("%s:%s/%d", host, tasklistPort, jobKey);
  }

  public static String getOnboardingFormURL(String accessKey) {
    return String.format("%s:%s/form?key=%s", host, frontendPort, accessKey);
  }

  public static String getUploadContractFormURL(String processId) {
    return String.format("%s:%s/upload-contract?processId=%s", host, frontendPort, processId);
  }

  public static String getCompanyLogoURL() {
    return String.format("%s:%s/file/syone-logo", host, backendPort);
  }

  public static String getCompanySignatureURL() {
    return String.format("%s:%s/file/company-signature", host, backendPort);
  }
}
