package com.example.OnlineService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.OnlineService.models.Project;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    Project findByIdentifier(long identifier);
}
