package com.adrian.webapp;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping("/feedback/view")
    public String viewFeedback(Model model) {
        List<Feedback> feedbackList = feedbackRepository.findAll();
        model.addAttribute("feedbackList", feedbackList);
        return "viewFeedback";
    }
    
    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedbackForm";
    }

    @PostMapping("/feedback")
    public String submitFeedback(@ModelAttribute("feedback") Feedback feedback) {
        feedback.setTimestamp(LocalDateTime.now());
        feedbackRepository.save(feedback);
        return "redirect:/feedback/thanks";
    }
    
    @GetMapping("/feedback/delete")
    public String deleteFeedback(@RequestParam int id, Model model) {
        feedbackRepository.deleteById(id);
        return "redirect:/feedback/view";
    }

    @GetMapping("/feedback/thanks")
    public String showThanksPage() {
        return "thanks";
    }
}
