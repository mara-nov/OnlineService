package com.example.OnlineService.repositories;

import com.example.OnlineService.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Tasks, Long> {
}
