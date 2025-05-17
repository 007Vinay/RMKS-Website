package com.rmks.website.controller;

import com.rmks.website.model.News;
import com.rmks.website.model.Feedback;
import com.rmks.website.model.Contact;
import com.rmks.website.service.NewsService;
import com.rmks.website.service.FeedbackService;
import com.rmks.website.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.time.LocalDateTime;

@Controller
public class MainController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private ContactService contactService;

    @GetMapping("/")
    public String home(Model model,
                       @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        boolean isHindi = "hi".equals(language);
        List<News> latestNews = newsService.getLatestNews(3);

        model.addAttribute("isHindi", isHindi);
        model.addAttribute("latestNews", latestNews);
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model,
                        @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        return "about";
    }

    @GetMapping("/mission")
    public String mission(Model model,
                          @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        return "mission";
    }

    @GetMapping("/founders")
    public String founders(Model model,
                           @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        return "founders";
    }

    @GetMapping("/services")
    public String services(Model model,
                           @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        return "services";
    }

    @GetMapping("/news")
    public String news(Model model,
                      @RequestParam(required = false) String category,
                      @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        
        // Get all news
        List<News> allNews = newsService.getAllNews();
        
        // Filter by category if specified
        if (category != null && !category.isEmpty()) {
            allNews = allNews.stream()
                    .filter(news -> category.equalsIgnoreCase(news.getCategory()))
                    .collect(Collectors.toList());
        }
        
        // Find featured news (first featured news if exists)
        News featuredNews = allNews.stream()
                .filter(News::isFeatured)
                .findFirst()
                .orElse(null);
                
        // Remove featured news from the main list if exists
        List<News> newsList = allNews.stream()
                .filter(news -> !news.isFeatured() || !news.equals(featuredNews))
                .toList();
        
        model.addAttribute("featuredNews", featuredNews);
        model.addAttribute("newsList", newsList);
        model.addAttribute("selectedCategory", category);
        return "news";
    }

    @GetMapping("/news/{id}")
    public String viewNews(@PathVariable Long id, Model model,
                         @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        model.addAttribute("isHindi", "hi".equals(language));
        return "news-detail";
    }

    @GetMapping("/feedback")
    public String feedback(Model model,
                           @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        return "feedback";
    }

    @GetMapping("/contact")
    public String contact(Model model,
                          @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        model.addAttribute("isHindi", "hi".equals(language));
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/feedback/submit")
    @ResponseBody
    public ResponseEntity<?> submitFeedback(@RequestBody Map<String, String> feedbackData) {
        try {
            Feedback feedback = new Feedback();
            feedback.setName(feedbackData.get("name"));
            feedback.setEmail(feedbackData.get("email"));
            feedback.setPhone(feedbackData.get("phone"));
            feedback.setMessage(feedbackData.get("message"));
            feedback.setCategory(feedbackData.get("category"));
            feedback.setProcessed(false);
            feedback.setSubmissionDate(LocalDateTime.now());
            feedback.setStatus("NEW");

            feedbackService.saveFeedback(feedback);

            return ResponseEntity.ok(Map.of("message", "Feedback submitted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/contact/submit")
    @ResponseBody
    public ResponseEntity<?> submitContact(@RequestBody Map<String, String> contactData) {
        try {
            Contact contact = new Contact();
            contact.setName(contactData.get("name"));
            contact.setEmail(contactData.get("email"));
            contact.setSubject(contactData.get("subject"));
            contact.setMessage(contactData.get("message"));
            contact.setProcessed(false);
            contact.setSubmissionDate(LocalDateTime.now());

            contactService.saveContact(contact);

            return ResponseEntity.ok(Map.of("message", "Message sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}