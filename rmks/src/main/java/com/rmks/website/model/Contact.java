package com.rmks.website.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime submissionDate;

    private boolean processed = false;

    @Column(length = 1000)
    private String adminResponse;

    private LocalDateTime responseDate;
} 