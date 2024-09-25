package org.example.camunda.process.solution.repository;

import org.example.camunda.process.solution.model.CamundaProcess;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CamundaProcessRepository extends JpaRepository<CamundaProcess, String> {}
