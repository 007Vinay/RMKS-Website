package com.rmks.website.service;

import com.rmks.website.model.Feedback;
import java.util.List;

public interface FeedbackService {
    List<Feedback> getAllFeedback();
    List<Feedback> getAllUnprocessedFeedback();
    Feedback getFeedbackById(Long id);
    Feedback saveFeedback(Feedback feedback);
    void deleteFeedback(Long id);
    long getUnprocessedFeedbackCount();
    long getMembershipRequestsCount();
    void processFeedback(Long id);

    void updateStatusAndPriority(Long id, String status, String priority);

    void addAdminResponse(Long id, String response, boolean sendEmail);

    List<Feedback> getAllFeedbacks();
}
