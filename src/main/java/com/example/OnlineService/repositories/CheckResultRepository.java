package com.example.OnlineService.repositories;


import com.example.OnlineService.models.CheckResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CheckResultRepository extends JpaRepository<CheckResult, Long> {
    List<CheckResult> findByWorkId(Long workId);

}
