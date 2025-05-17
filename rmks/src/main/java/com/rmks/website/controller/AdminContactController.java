package com.rmks.website.controller;

import com.rmks.website.model.Contact;
import com.rmks.website.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/contact")
public class AdminContactController {

    @Autowired
    private ContactService contactService;

    @GetMapping
    public String listContacts(Model model) {
        model.addAttribute("contactList", contactService.getAllContacts());
        return "admin/contact-list";
    }

    @GetMapping("/view/{id}")
    public String viewContact(@PathVariable Long id, Model model) {
        Contact contact = contactService.getContactById(id);
        model.addAttribute("contact", contact);
        return "admin/contact-details";
    }

    @PostMapping("/respond")
    public String respondToContact(@RequestParam Long id,
                                 @RequestParam String response,
                                 @RequestParam(defaultValue = "false") boolean sendEmail,
                                 RedirectAttributes redirectAttributes) {
        try {
            contactService.addAdminResponse(id, response, sendEmail);
            redirectAttributes.addFlashAttribute("success", "Response sent successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error sending response: " + e.getMessage());
        }
        return "redirect:/admin/contact/view/" + id;
    }

    @PostMapping("/delete")
    public String deleteContact(@RequestParam Long id, RedirectAttributes redirectAttributes) {
        try {
            contactService.deleteContact(id);
            redirectAttributes.addFlashAttribute("success", "Contact deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting contact: " + e.getMessage());
        }
        return "redirect:/admin/contact";
    }
} 