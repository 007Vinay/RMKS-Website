package com.rmks.website.service.impl;

import com.rmks.website.model.Contact;
import com.rmks.website.repository.ContactRepository;
import com.rmks.website.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Override
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    @Override
    public List<Contact> getUnprocessedContacts() {
        return contactRepository.findByProcessedFalse();
    }

    @Override
    public Contact getContactById(Long id) {
        return contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    @Override
    public Contact saveContact(Contact contact) {
        if (contact.getSubmissionDate() == null) {
            contact.setSubmissionDate(LocalDateTime.now());
        }
        return contactRepository.save(contact);
    }

    @Override
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    @Override
    public long getUnprocessedContactCount() {
        return contactRepository.countByProcessedFalse();
    }

    @Override
    public void processContact(Long id) {
        Contact contact = getContactById(id);
        contact.setProcessed(true);
        contactRepository.save(contact);
    }

    @Override
    public void addAdminResponse(Long id, String response, boolean sendEmail) {
        Contact contact = getContactById(id);
        contact.setAdminResponse(response);
        contact.setProcessed(true);
        contact.setResponseDate(LocalDateTime.now());
        contactRepository.save(contact);

        // Optional: Implement email sending if sendEmail is true
        if (sendEmail) {
            // emailService.sendContactResponse(contact.getEmail(), response);
            // For now, log or placeholder
            System.out.println("Email would be sent to: " + contact.getEmail());
        }
    }
} 