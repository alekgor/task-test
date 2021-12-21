package com.alekgor.task.model.jpa.repository;

import com.alekgor.task.model.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepo extends JpaRepository<Quote, Long> {

}
