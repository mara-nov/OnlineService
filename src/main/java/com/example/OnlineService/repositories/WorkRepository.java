package com.example.OnlineService.repositories;


import com.example.OnlineService.models.GroupWork;
import com.example.OnlineService.models.Work;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkRepository extends JpaRepository<Work, Long> {
    Optional<Work> findById(Long workId);
    List<Work> findByGroupId(Long groupId);
}
