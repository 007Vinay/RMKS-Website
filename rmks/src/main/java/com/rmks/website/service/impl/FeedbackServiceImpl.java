package com.rmks.website.service.impl;

import com.rmks.website.model.Feedback;
import com.rmks.website.repository.FeedbackRepository;
import com.rmks.website.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public List<Feedback> getAllUnprocessedFeedback() {
        return feedbackRepository.findByProcessedFalse();
    }

    @Override
    public Feedback getFeedbackById(Long id) {
        return feedbackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Feedback not found with id: " + id));
    }

    @Override
    public Feedback saveFeedback(Feedback feedback) {
        if (feedback.getSubmissionDate() == null) {
            feedback.setSubmissionDate(LocalDateTime.now());
        }
        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }

    @Override
    public long getUnprocessedFeedbackCount() {
        return feedbackRepository.countByProcessedFalse();
    }

    @Override
    public long getMembershipRequestsCount() {
        return feedbackRepository.countByProcessedFalseAndCategory("MEMBERSHIP_REQUEST");
    }

    @Override
    public void processFeedback(Long id) {
        Feedback feedback = getFeedbackById(id);
        feedback.setProcessed(true);
        feedbackRepository.save(feedback);
    }

    @Override
    public void updateStatusAndPriority(Long id, String status, String priority) {
        Feedback feedback = getFeedbackById(id);
        feedback.setStatus(status);
        feedback.setPriority(priority);
        feedbackRepository.save(feedback);
    }

    @Override
    public void addAdminResponse(Long id, String response, boolean sendEmail) {
        Feedback feedback = getFeedbackById(id);
        feedback.setAdminResponse(response);
        feedback.setProcessed(true);
        feedbackRepository.save(feedback);

        // Optional: Implement email sending if sendEmail is true
        if (sendEmail) {
            // emailService.sendFeedbackResponse(feedback.getEmail(), response);
            // For now, log or placeholder
            System.out.println("Email would be sent to: " + feedback.getEmail());
        }
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
}
