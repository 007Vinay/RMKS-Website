package com.rmks.website.repository;

import com.rmks.website.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByProcessedFalse();
    long countByProcessedFalse();
} 