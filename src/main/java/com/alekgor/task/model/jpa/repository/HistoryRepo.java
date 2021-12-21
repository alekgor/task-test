package com.alekgor.task.model.jpa.repository;

import com.alekgor.task.model.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepo extends JpaRepository<History, Long> {
}
