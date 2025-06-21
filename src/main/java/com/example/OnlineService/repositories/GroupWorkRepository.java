package com.example.OnlineService.repositories;

import com.example.OnlineService.models.GroupWork;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GroupWorkRepository extends JpaRepository<GroupWork, Long> {
    Optional<GroupWork> findById(Long groupWorkId);


}
