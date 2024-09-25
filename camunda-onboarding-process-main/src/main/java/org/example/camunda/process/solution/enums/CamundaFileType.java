package org.example.camunda.process.solution.enums;

public enum CamundaFileType {
  PHOTO("photo"),
  CERTIFICATE_OF_QUALIFICATIONS("certificate-of-qualifications"),
  CONTRACT("contract"),
  SITI_USAGE_POLICY("siti-usage-policy"),
  IHT_AGREEMENT("iht-agreement"),
  ADITAMENTO("aditamento"),
  IMAGE_USE_AUTHORIZATION("image-use-authorization");

  private String type;

  CamundaFileType(String type) {
    this.type = type;
  }

  public String getType() {
    return this.type;
  }

  public static CamundaFileType fromType(String type) {
    for (CamundaFileType cft : CamundaFileType.values()) {
      if (cft.getType().equals(type)) return cft;
    }
    return null;
  }
}
