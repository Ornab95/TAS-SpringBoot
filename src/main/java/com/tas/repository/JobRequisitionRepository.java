package com.tas.repository;

import com.tas.entity.JobRequisition;
import com.tas.enums.RequisitionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRequisitionRepository extends JpaRepository<JobRequisition, Long> {
    List<JobRequisition> findByStatus(RequisitionStatus status);
    List<JobRequisition> findByCreatedById(Long userId);
    List<JobRequisition> findByDepartment(String department);
    List<JobRequisition> findByStatusIn(List<RequisitionStatus> statuses);
}