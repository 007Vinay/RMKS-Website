package com.rmks.website.service.impl;

import com.rmks.website.model.Activity;
import com.rmks.website.repository.ActivityRepository;
import com.rmks.website.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Override
    public Activity logActivity(String description, String details, String actionType, String performedBy) {
        Activity activity = new Activity(description, details, actionType, performedBy);
        return activityRepository.save(activity);
    }

    @Override
    public List<Activity> getRecentActivities() {
        return activityRepository.findTop10ByOrderByTimestampDesc();
    }

    @Override
    public Page<Activity> getActivitiesByType(String actionType, Pageable pageable) {
        return activityRepository.findByActionTypeOrderByTimestampDesc(actionType, pageable);
    }
} 