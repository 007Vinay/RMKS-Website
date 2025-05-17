package com.rmks.website.service;

import com.rmks.website.model.Contact;
import java.util.List;

public interface ContactService {
    List<Contact> getAllContacts();
    List<Contact> getUnprocessedContacts();
    Contact getContactById(Long id);
    Contact saveContact(Contact contact);
    void deleteContact(Long id);
    long getUnprocessedContactCount();
    void processContact(Long id);
    void addAdminResponse(Long id, String response, boolean sendEmail);
} 