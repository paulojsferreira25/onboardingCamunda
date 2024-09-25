package org.example.camunda.process.solution.utils;

public class EmailBody {
  public static String getBuddyMailToNewcomerBody(
      String firstName, String buddyFullName, String buddyFirstName) {
    return String.format(
        "Olá %s,\n\nNa sequência da tua entrada na Syone, informo que o/a %s será o teu Buddy.\n\nO/A %s irá "
            + "ajudar-te na integração na empresa e a conheceres como trabalhamos e como vivemos a Syone!\n\n"
            + "Mais uma vez, bem-vindo/a à Syone\uD83D\uDE0A",
        firstName, buddyFullName, buddyFirstName);
  }

  public static String getBuddyMailToBuddyBody(
      String fullName, String firstName, String buddyFirstName) {
    return String.format(
        "Olá %s,\n"
            + " \n"
            + "Na sequência do que falámos, entrou para a Syone o/a %s.\n"
            + " \n"
            + "Como Buddy, tens a oportunidade de facilitar a sua integração e partilhar o teu conhecimento acerca da Syone, auxiliando em tarefas mais operacionais.\n"
            + " \n"
            + "Durante os próximos 3 meses, assumes um papel fundamental para o sucesso da carreira do/a %s na nossa empresa.\n"
            + " \n"
            + "Consulta as guidelines sobre o Buddy System (em anexo) onde estão descritas as principais funções do Buddy e ainda algumas dicas, tais como:\n"
            + " \n"
            + "\t• Contactar e conhecer o newcomer no Dia 1;\n"
            + "\t• Estabelecer contacto numa base semanal pelo menos durante o 1º Mês de integração;\n"
            + "\t• Enviar desde já os invites das vossas reuniões/calls para efeitos de agenda e organização;\n"
            + "\t• Contactar o Manager ou a equipa de People Xperience para ajudar ou desbloquear alguma situação.\n"
            + " \n"
            + "Obrigado pela tua colaboração e participação no programa!\n"
            + " \n"
            + "Cumprimentos.",
        buddyFirstName, fullName, firstName);
  }

  public static String getSendContractDocsToNewcomerBody(
      String aditamentoFilename,
      String imageUseAuthorizationFilename,
      String peopleAndCultureEmail) {
    String aditamentoString =
        (aditamentoFilename != null
            ? "\t- O Documento \""
                + aditamentoFilename
                + "\" é opcional, só assinas se tiveres interesse em receber os "
                + "subsídios de natal e férias por duodécimos (depois deixa aqui só a nota se pretendes ou não);\n"
            : "");
    return String.format(
        "Olá \n\nSegue em anexo a documentação contratual para assinares:\n\n%s\t- O Documento \"%s\" também é "
            + "opcional, é autorização para a divulgação de imagens em que estejas presente (eg. team buildings, foto "
            + "de entrada no onboarding, etc.).\n\nPeço que rubriques todas as páginas, assines os documentos no local "
            + "final indicado e envies para o seguinte email: %s.\n\nAlguma coisa não hesites em contactar \uD83D\uDE0A\n\n"
            + "Bem-vindo e obrigada!",
        aditamentoString, imageUseAuthorizationFilename, peopleAndCultureEmail);
  }

  public static String getUserTaskNotificationBody(
      String taskName, String fullName, String userTaskURL) {
    return String.format(
        "Task `%s` for the onboarding process of %s is now available to complete at %s.",
        taskName, fullName, userTaskURL);
  }

  public static String getSendOnboardingFormToNewcomerBody(String firstName, String formLink) {
    return String.format(
        "Caro %s,\n\nNa sequência do e-mail de aceitação da nossa proposta de colaboração, adianto que ficamos "
            + "muito felizes que tenhas aceite juntar-te à equipa da SYONE. Tenho a certeza que vais ter uma ótima "
            + "experiência de trabalho aqui. Bem-vindo!\n\nPara dar seguimento ao teu processo de entrada na SYONE e "
            + "elaboração do contrato de trabalho e gestão de recursos humanos, agradecemos que nos encaminhes as tuas "
            + "informações através do seguinte formulário: %s\n\n"
            + "Ainda informamos que: \n"
            + "1.  Os dados pessoais recolhidos serão utilizados para as seguintes finalidades: \n"
            + "a)  Redação do contrato de trabalho; \n"
            + "b)  Gestão do processo de trabalhador: \n"
            + "c)  Cumprimento de obrigações tributarias; \n"
            + "d)  Cumprimento de obrigações junto à segurança social. \n"
            + "2.  A SYONE SBS SOFTWARE - TECNOLOGIA E SERVIÇOS DE INFORMÁTICA, S.A., adiante SYONE, com o NIPC 504729624 e sede Rua Alfredo da Silva, 8-A, Edifício Stern, Piso 3D, 2610-016 Amadora, é a responsável pelo tratamento. \n"
            + "3.  Sem os dados pessoais solicitados não poderemos dar seguimento ao seu processo de contratação, pelo que o fornecimento dos mesmos é obrigatório. \n"
            + "4.  Os dados em questão serão conservados pela SYONE durante a execução do contrato de trabalho e durante o período de tempo necessário ao cumprimento das obrigações legais a que a SYONE se encontra vinculada, nomeadamente, obrigações fiscais e de segurança social. \n"
            + "5.  Poderá exercer os seus direitos previstos na legislação (acesso, retificação, apagamento, limitação, oposição, portabilidade) através de contacto escrito para o seguinte email data.privacy@syone.com, sem prejuízo do direito a apresentar reclamação à autoridade de controlo competente. \n"
            + "6.  Quando nos envia os seus dados pessoais, estes serão conservados num servidor seguro e de acesso condicionado, sendo apenas acedidos pelas pessoas devidamente autorizadas a ter acesso aos mesmos. \n"
            + "7.  A SYONE apenas comunicará os dados identificados ao(s) seu(s) prestador(es) de serviços que lhe prestem apoio no âmbito das finalidades indicadas, nomeadamente, gestão de recursos humanos, processamento salarial, etc.. \n"
            + "8.  Os seus dados pessoais serão ainda transmitidos a terceiros, nomeadamente, Autoridade Tributária e Segurança Social, no âmbito do cumprimento das obrigações legais da SYONE. \n"
            + "9.  Caso utilizemos os seus dados pessoais para outras finalidades que não os indicados, iremos informá-lo quanto a esses tratamentos em momento anterior à utilização dos dados pessoais. \n"
            + "10. Se quiser obter mais informações mais sobre a forma como os seus dados pessoais serão tratados, contacte-nos através do email data.privacy@syone.com. \n\n"
            + "Qualquer questão adicional, não hesites em contactar-nos!\n\nCumprimentos e bem-vindo novamente.",
        firstName, formLink);
  }
}
