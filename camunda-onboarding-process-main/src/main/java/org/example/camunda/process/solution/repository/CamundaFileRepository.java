package org.example.camunda.process.solution.repository;

import org.example.camunda.process.solution.model.CamundaFile;
import org.example.camunda.process.solution.model.CamundaFileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CamundaFileRepository extends JpaRepository<CamundaFile, CamundaFileId> {
  @Query(value = "SELECT * FROM camunda_file WHERE process_id=?1 AND type=?2", nativeQuery = true)
  CamundaFile findByProcessIdAndType(String processId, String type);
}
