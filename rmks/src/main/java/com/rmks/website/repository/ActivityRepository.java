package com.rmks.website.repository;

import com.rmks.website.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findTop10ByOrderByTimestampDesc();
    Page<Activity> findByActionTypeOrderByTimestampDesc(String actionType, Pageable pageable);
} 