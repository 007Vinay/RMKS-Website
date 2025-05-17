package com.rmks.website.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(length = 1000)
    private String message;

    private String category; // FEEDBACK, MEMBERSHIP_REQUEST, VOLUNTEER_APPLICATION

    @Column(nullable = false)
    private LocalDateTime submissionDate;

    private boolean processed = false;

    private String status;   // NEW, IN_PROGRESS, RESOLVED, CLOSED
    private String priority; // LOW, MEDIUM, HIGH

    @Column(length = 1000)
    private String adminResponse;
}
