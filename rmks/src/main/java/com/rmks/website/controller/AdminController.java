package com.rmks.website.controller;

import com.rmks.website.model.Feedback;
import com.rmks.website.model.Member;
import com.rmks.website.model.News;
import com.rmks.website.service.NewsService;
import com.rmks.website.service.FeedbackService;
import com.rmks.website.service.MemberService;
import com.rmks.website.service.FileStorageService;
import com.rmks.website.service.ActivityService;
import com.rmks.website.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactService contactService;

    @GetMapping("")
    public String dashboard(Model model) {
        model.addAttribute("newsCount", newsService.getNewsCount());
        model.addAttribute("unprocessedFeedbackCount", feedbackService.getUnprocessedFeedbackCount());
        model.addAttribute("membershipRequestsCount", feedbackService.getMembershipRequestsCount());
        model.addAttribute("unprocessedContactCount", contactService.getUnprocessedContactCount());
        model.addAttribute("recentActivities", activityService.getRecentActivities());
        model.addAttribute("recentFeedback", feedbackService.getAllUnprocessedFeedback());
        model.addAttribute("recentContacts", contactService.getUnprocessedContacts());
        return "admin/dashboard";
    }

    @GetMapping("/news")
    public String viewNewsList(Model model) {
        List<News> newsList = newsService.getAllNews();
        model.addAttribute("newsList", newsList);
        return "admin/news-list";
    }

    @GetMapping("/news/create")
    public String createNewsForm(Model model) {
        model.addAttribute("news", new News());
        return "admin/news-form";
    }

    @PostMapping("/news/save")
    public String saveNews(@ModelAttribute News news,
                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                         @RequestParam(value = "publishDate", required = false) String publishDate,
                         @RequestParam(value = "eventDate", required = false) String eventDate,
                         Model model) throws IOException {
        try {
            // Debug logging
            System.out.println("Received news data:");
            System.out.println("Title EN: " + news.getTitleEn());
            System.out.println("Title HI: " + news.getTitleHi());
            System.out.println("Publish Date: " + publishDate);
            System.out.println("Event Date: " + eventDate);
            System.out.println("Image File: " + (imageFile != null ? imageFile.getOriginalFilename() : "no file"));

            // Handle image upload
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = uploadImage(imageFile);
                news.setImageUrl(imageUrl);
            }
            
            // Handle publish date
            try {
                if (publishDate != null && !publishDate.isEmpty()) {
                    System.out.println("Parsing publish date: " + publishDate);
                    LocalDateTime parsedPublishDate = LocalDateTime.parse(publishDate);
                    news.setPublishDate(parsedPublishDate);
                } else {
                    news.setPublishDate(LocalDateTime.now());
                }
            } catch (Exception e) {
                System.out.println("Error parsing publish date: " + publishDate);
                e.printStackTrace();
                news.setPublishDate(LocalDateTime.now());
            }
            
            // Handle event date
            if (eventDate != null && !eventDate.isEmpty()) {
                try {
                    System.out.println("Parsing event date: " + eventDate);
                    LocalDateTime parsedEventDate = LocalDateTime.parse(eventDate);
                    news.setEventDate(parsedEventDate);
                } catch (Exception e) {
                    System.out.println("Error parsing event date: " + eventDate);
                    e.printStackTrace();
                }
            }
            
            // Ensure required fields are set
            if (news.getTitleEn() == null || news.getTitleEn().trim().isEmpty()) {
                throw new IllegalArgumentException("English title is required");
            }
            if (news.getTitleHi() == null || news.getTitleHi().trim().isEmpty()) {
                throw new IllegalArgumentException("Hindi title is required");
            }
            
            // Save the news
            News savedNews = newsService.saveNews(news);
            
            // Log the activity
            String action = news.getId() == null ? "Created" : "Updated";
            activityService.logActivity(
                action + " News Article",
                action + " news article: " + news.getTitleEn(),
                "NEWS",
                "admin"
            );
            
            return "redirect:/admin/news";
            
        } catch (Exception e) {
            System.out.println("Error saving news: ");
            e.printStackTrace();
            model.addAttribute("error", "Error saving news: " + e.getMessage());
            model.addAttribute("news", news);
            return "admin/news-form";
        }
    }

    @GetMapping("/members")
    public String viewMembershipRequests(Model model) {
        List<Feedback> membershipRequests = feedbackService.getAllFeedback().stream()
                .filter(f -> "MEMBERSHIP_REQUEST".equals(f.getCategory()) && !f.isProcessed())
                .toList();
                
        List<Member> members = membershipRequests.stream()
                .map(f -> {
                    Member member = new Member();
                    member.setName(f.getName());
                    member.setEmail(f.getEmail());
                    member.setPhone(f.getPhone());
                    member.setMessage(f.getMessage());
                    member.setCreatedAt(f.getSubmissionDate());
                    member.setStatus("PENDING");
                    member.setFeedbackId(f.getId());
                    return member;
                })
                .toList();
                
        model.addAttribute("members", members);
        return "admin/members";
    }

    @PostMapping("/members/approve")
    public String approveMember(@RequestParam Long id) {
        try {
            // First, get the feedback
            Feedback feedback = feedbackService.getFeedbackById(id);
            if (feedback == null) {
                throw new RuntimeException("Feedback not found with id: " + id);
            }

            // Create and save the member
            Member member = new Member();
            member.setName(feedback.getName());
            member.setEmail(feedback.getEmail());
            member.setPhone(feedback.getPhone());
            member.setMessage(feedback.getMessage());
            member.setStatus("APPROVED");
            member.setApproved(true);
            member.setCreatedAt(feedback.getSubmissionDate());
            memberService.saveMemberRequest(member);

            // Mark the feedback as processed
            feedback.setProcessed(true);
            feedback.setStatus("APPROVED");
            feedbackService.saveFeedback(feedback);

            // Log the activity
            activityService.logActivity(
                "Membership Request Approved",
                "Approved membership request for " + member.getName(),
                "MEMBER",
                "admin"
            );

            return "redirect:/admin/members";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/members";
        }
    }

    @PostMapping("/members/reject")
    public String rejectMember(@RequestParam Long id) {
        try {
            // Get and update the feedback
            Feedback feedback = feedbackService.getFeedbackById(id);
            if (feedback == null) {
                throw new RuntimeException("Feedback not found with id: " + id);
            }

            feedback.setProcessed(true);
            feedback.setStatus("REJECTED");
            feedbackService.saveFeedback(feedback);

            // Log the activity
            activityService.logActivity(
                "Membership Request Rejected",
                "Rejected membership request from " + feedback.getName(),
                "MEMBER",
                "admin"
            );

            return "redirect:/admin/members";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/members";
        }
    }

    @GetMapping("/news/edit/{id}")
    public String editNews(@PathVariable Long id, Model model) {
        News news = newsService.getNewsById(id);
        model.addAttribute("news", news);
        return "admin/news-form";
    }

    @GetMapping("/news/delete/{id}")
    public String deleteNews(@PathVariable Long id) {
        News news = newsService.getNewsById(id);
        newsService.deleteNews(id);
        
        // Log the activity
        activityService.logActivity(
            "Deleted News Article",
            "Deleted news article: " + news.getTitleEn(),
            "NEWS",
            "admin"
        );
        
        return "redirect:/admin/news";
    }

    @GetMapping("/feedback")
    public String viewFeedbackPage(Model model,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String priority,
                                 @RequestParam(required = false) String category) {
        List<Feedback> feedbackList = feedbackService.getAllFeedback();
        
        // Apply filters if they are set
        if (status != null && !status.isEmpty()) {
            feedbackList = feedbackList.stream()
                    .filter(f -> status.equals(f.getStatus()))
                    .toList();
        }
        
        if (priority != null && !priority.isEmpty()) {
            feedbackList = feedbackList.stream()
                    .filter(f -> priority.equals(f.getPriority()))
                    .toList();
        }
        
        if (category != null && !category.isEmpty()) {
            feedbackList = feedbackList.stream()
                    .filter(f -> category.equals(f.getCategory()))
                    .toList();
        }

        model.addAttribute("feedbackList", feedbackList);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedPriority", priority);
        model.addAttribute("selectedCategory", category);
        return "admin/feedback";
    }

    // View feedback detail page
    @GetMapping("/feedback/view/{id}")
    public String viewFeedbackDetail(@PathVariable Long id, Model model,
                                   @CookieValue(name = "preferredLanguage", defaultValue = "en") String language) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        model.addAttribute("feedback", feedback);
        model.addAttribute("isHindi", "hi".equals(language));
        return "admin/feedback-details";
    }

    // Update feedback status and priority
    @PostMapping("/feedback/update-status")
    public String updateFeedbackStatus(@RequestParam Long id,
                                       @RequestParam String status,
                                       @RequestParam String priority) {
        feedbackService.updateStatusAndPriority(id, status, priority);
        return "redirect:/admin/feedback";
    }

    // Respond to feedback and optionally send email
    @PostMapping("/feedback/respond")
    public String respondToFeedback(@RequestParam Long id,
                                    @RequestParam String response,
                                    @RequestParam(required = false, defaultValue = "false") boolean sendEmail) {
        Feedback feedback = feedbackService.getFeedbackById(id);
        feedbackService.addAdminResponse(id, response, sendEmail);
        
        // Log the activity
        activityService.logActivity(
            "Responded to Feedback",
            "Responded to feedback from " + feedback.getName(),
            "FEEDBACK",
            "admin"
        );
        
        return "redirect:/admin/feedback";
    }

    // Delete feedback
    @PostMapping("/feedback/delete")
    public String deleteFeedback(@RequestParam Long id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/admin/feedback";
    }

    private String uploadImage(MultipartFile file) throws IOException {
        try {
            // Debug logging
            System.out.println("Uploading file: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("Content type: " + file.getContentType());

            // Store the file
            String filename = fileStorageService.storeFile(file);
            System.out.println("File stored successfully: " + filename);
            
            // Return the URL path
            return "/uploads/" + filename;
        } catch (Exception e) {
            System.err.println("Error uploading file: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private Member feedbackToMember(Feedback feedback) {
        Member member = new Member();
        member.setName(feedback.getName());
        member.setEmail(feedback.getEmail());
        member.setPhone(feedback.getPhone());
        member.setMessage(feedback.getMessage());
        member.setCreatedAt(feedback.getSubmissionDate());
        member.setStatus("PENDING");
        member.setFeedbackId(feedback.getId());
        return member;
    }
}
