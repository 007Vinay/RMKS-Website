package com.rmks.website.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titleEn;

    @Column(nullable = false)
    private String titleHi;

    @Column(length = 500)
    private String summaryEn; // Added field for English summary

    @Column(length = 500)
    private String summaryHi; // Optional: summary in Hindi

    @Column(length = 2000)
    private String contentEn;

    @Column(length = 2000)
    private String contentHi;

    private String imageUrl;

    @Column(nullable = false)
    private LocalDateTime publishDate;

    private LocalDateTime eventDate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 50)
    private String category;

    @Column(nullable = false)
    private boolean featured = false;
}
