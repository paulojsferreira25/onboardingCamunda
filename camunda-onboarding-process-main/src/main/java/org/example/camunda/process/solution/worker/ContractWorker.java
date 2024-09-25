package org.example.camunda.process.solution.worker;

import io.camunda.zeebe.client.api.response.ActivatedJob;
import io.camunda.zeebe.spring.client.annotation.JobWorker;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.example.camunda.process.solution.enums.CamundaFileType;
import org.example.camunda.process.solution.exception.SpringCamundaException;
import org.example.camunda.process.solution.service.CamundaFileService;
import org.example.camunda.process.solution.utils.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ContractWorker {
  private static final Logger LOG = LoggerFactory.getLogger(ContractWorker.class);
  private final CamundaFileService camundaFileService;

  public ContractWorker(CamundaFileService camundaFileService) {
    this.camundaFileService = camundaFileService;
  }

  @JobWorker
  public Map<String, String> prepareContract(final ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String processId = variables.get("processId").toString();
    String templateName = variables.get("contractTemplate").toString();
    prepareDocument(variables, CamundaFileType.CONTRACT, "Contrato de Trabalho", templateName);

    Map<String, String> returnVariables = new HashMap<>();
    returnVariables.put("contractLink", URL.getContractURL(processId));
    return returnVariables;
  }

  @JobWorker
  public Map<String, String> prepareOtherContractDocs(ActivatedJob job) {
    Map<String, Object> variables = job.getVariablesAsMap();
    String processId = variables.get("processId").toString();

    // prepare docs
    prepareDocument(
        variables, CamundaFileType.SITI_USAGE_POLICY, "SI-TI Syone", "siti-politica-utilizacao");
    prepareDocument(
        variables, CamundaFileType.IHT_AGREEMENT, "Acordo de Isenção de Horário", "acordo-iht");
    if (!variables.get("duodecimos").equals("No"))
      prepareDocument(variables, CamundaFileType.ADITAMENTO, "Aditamento", "aditamento");
    prepareDocument(
        variables,
        CamundaFileType.IMAGE_USE_AUTHORIZATION,
        "Autorização Utilização Imagens",
        "autorizacao-utilizacao-imagens");

    // add document links to process
    Map<String, String> returnVariables = new HashMap<>();
    returnVariables.put("usagePolicyLink", URL.getSITIUsagePolicyURL(processId));
    returnVariables.put("IHTAgreementLink", URL.getIHTAgreementURL(processId));
    if (!variables.get("duodecimos").equals("No"))
      returnVariables.put("aditamentoLink", URL.getAditamentoURL(processId));
    returnVariables.put("imageUseAuthorizationLink", URL.getImageUseAuthorizationURL(processId));
    return returnVariables;
  }

  private void prepareDocument(
      Map<String, Object> variables,
      CamundaFileType type,
      String destFilenamePrefix,
      String templateName) {
    String processId = variables.get("processId").toString();

    String fullName = variables.get("fullName").toString();
    String[] names = fullName.split(" ");

    String startDate = variables.get("startDate").toString();
    String[] splitStartDate = startDate.split("-");
    String fileDate = splitStartDate[2] + "_" + splitStartDate[1] + "_" + splitStartDate[0];
    String destFilename =
        String.format(
            "%s_%s_%s_%s.pdf", destFilenamePrefix, names[0], names[names.length - 1], fileDate);

    String sourceFilePath = "src/main/resources/templates/" + templateName + ".jrxml";
    Map<String, Object> parameters = putParameters(variables, templateName);

    try {
      // prepare doc
      LOG.info(String.format("[%s] Preparing %s.", processId, destFilename));
      JasperReport jasperReport = JasperCompileManager.compileReport(sourceFilePath);
      JasperPrint jasperPrint =
          JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

      // save doc in db
      LOG.info(String.format("[%s] Storing %s in database.", processId, destFilename));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      JasperExportManager.exportReportToPdfStream(jasperPrint, baos);
      camundaFileService.store(processId, destFilename, type, baos.toByteArray());
      baos.close();

      LOG.info(String.format("[%s] Document prepared and stored.", processId));

    } catch (JRException | IOException e) {
      LOG.error(
          String.format(
              "[%s] An error occurred during the processing of %s.", processId, destFilename));
      throw new SpringCamundaException(
          "An error occurred during the processing of " + destFilename + ".");
    }
  }

  private Map<String, Object> putParameters(Map<String, Object> variables, String templateName) {
    String[] splitIdentificationValidity =
        variables.get("identificationValidity").toString().split("-");
    String identificationValidity =
        splitIdentificationValidity[2]
            + "/"
            + splitIdentificationValidity[1]
            + "/"
            + splitIdentificationValidity[0];

    String[] splitStartDate = variables.get("startDate").toString().split("-");
    String startDate = getCustomDateFormat(splitStartDate[2], splitStartDate[1], splitStartDate[0]);

    DecimalFormat twoDecimalCases = new DecimalFormat("0.00");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("fullName", variables.get("fullName").toString());
    parameters.put("maritalStatus", variables.get("maritalStatus").toString());
    parameters.put("identificationNumber", variables.get("identificationNumber").toString());
    parameters.put("identificationValidity", identificationValidity);
    parameters.put("nif", variables.get("nif").toString());
    parameters.put("niss", variables.get("niss").toString());
    parameters.put("address", variables.get("address").toString());
    parameters.put("postalCode", variables.get("postalCode").toString());
    parameters.put("startDate", startDate);
    parameters.put(
        "baseSalary",
        twoDecimalCases
            .format(Float.parseFloat(variables.get("baseSalary").toString()))
            .replace('.', ','));
    parameters.put("baseSalaryInWords", variables.get("baseSalaryInWords").toString());
    parameters.put(
        "iht",
        twoDecimalCases
            .format(Float.parseFloat(variables.get("iht").toString()))
            .replace('.', ','));
    parameters.put("ihtInWords", variables.get("ihtInWords").toString());
    parameters.put("client", variables.get("client").toString());
    parameters.put("syoneLogoURL", URL.getCompanyLogoURL());
    parameters.put("companySignatureURL", URL.getCompanySignatureURL());

    if (templateName.equals("nacional-termo-certo")) {
      parameters.put("contractDuration", variables.get("contractDuration").toString());

      String[] splitEndDate = variables.get("contractEndDate").toString().split("-");
      String endDate = getCustomDateFormat(splitEndDate[2], splitEndDate[1], splitEndDate[0]);
      parameters.put("endDate", endDate);
    }
    return parameters;
  }

  private String getCustomDateFormat(String day, String month, String year) {
    return day + " de " + getMonth(month) + " de " + year;
  }

  private String getMonth(String month) {
    switch (month) {
      case "01":
        return "janeiro";
      case "02":
        return "fevereiro";
      case "03":
        return "março";
      case "04":
        return "abril";
      case "05":
        return "maio";
      case "06":
        return "junho";
      case "07":
        return "julho";
      case "08":
        return "agosto";
      case "09":
        return "setembro";
      case "10":
        return "outubro";
      case "11":
        return "novembro";
      case "12":
        return "dezembro";
      default:
        return "mês";
    }
  }
}
