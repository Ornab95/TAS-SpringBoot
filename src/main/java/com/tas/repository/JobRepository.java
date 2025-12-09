package com.tas.repository;

import com.tas.entity.Job;
import com.tas.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByActiveTrue();
    List<Job> findByPublishedTrue();
    List<Job> findByDepartment(String department);
    List<Job> findByPostedById(Long userId);
    List<Job> findByPostedBy(User user);
    List<Job> findByActiveTrueAndPublishedTrue();
    Long countByActiveTrue();
}