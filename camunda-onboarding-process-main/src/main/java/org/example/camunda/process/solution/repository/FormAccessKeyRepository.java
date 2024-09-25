package org.example.camunda.process.solution.repository;

import org.example.camunda.process.solution.model.FormAccessKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormAccessKeyRepository extends JpaRepository<FormAccessKey, String> {}
