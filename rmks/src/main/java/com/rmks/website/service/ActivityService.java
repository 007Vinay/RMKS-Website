package com.rmks.website.service;

import com.rmks.website.model.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ActivityService {
    Activity logActivity(String description, String details, String actionType, String performedBy);
    List<Activity> getRecentActivities();
    Page<Activity> getActivitiesByType(String actionType, Pageable pageable);
} 