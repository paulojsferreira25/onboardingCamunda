package org.example.camunda.process.solution.dto;

import lombok.Data;
import org.apache.commons.lang3.builder.MultilineRecursiveToStringStyle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.camunda.process.solution.utils.URL;

@Data
public class NewcomerDTO {
  private String processId;
  private String fullName;
  private String dateOfBirth;
  private String emailAddress;
  private String identificationNumber;
  private String identificationValidity;
  private String nif;
  private String niss;
  private String address;
  private String postalCode;
  private String maritalStatus;
  private int numberOfDependents;
  private String iban;
  private String startDate;
  private String photoLink = "";
  private String certificateOfQualificationsLink = "";
  private String contractLink = "";
  private String individualRecordLink = URL.getIndividualRecordURL();
  private String uploadContractFormLink = "";

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(
        this,
        new MultilineRecursiveToStringStyle() {
          public ToStringStyle withShortPrefixes() {
            this.setUseShortClassName(true);
            this.setUseIdentityHashCode(false);
            return this;
          }
        }.withShortPrefixes());
  }
}
