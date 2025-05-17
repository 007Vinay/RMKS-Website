package com.rmks.website.repository;

import com.rmks.website.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    List<Feedback> findByProcessedFalse();
    long countByProcessedFalse();
    long countByProcessedFalseAndCategory(String category);
}
