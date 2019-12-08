package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Comment;
import ru.itmo.wp.security.Guest;
import ru.itmo.wp.service.CommentService;
import ru.itmo.wp.service.PostService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PostPage extends Page {
    private final PostService postService;

    public PostPage(PostService postService) {
        this.postService = postService;
    }

    @Guest
    @GetMapping("/post/{id}")
    public String post(@PathVariable String id, Model model) {
        try {
            model.addAttribute("post", postService.findById(Long.parseLong(id)));
            model.addAttribute("comment", new Comment());
        } catch (NumberFormatException ignored) {
            //do nothing
        }
        return "PostPage";
    }

    @PostMapping("/post/{id}")
    public String writeComment(@PathVariable String id, @Valid @ModelAttribute("comment") Comment comment,
                               BindingResult bindingResult, HttpSession httpSession, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", postService.findById(Long.parseLong(id)));
            return "PostPage";
        }

        comment.setUser(getUser(httpSession));
        postService.writeComment(postService.findById(Long.parseLong(id)), comment);
        putMessage(httpSession, "You created new comment");
        return "redirect:/post/" + id;
    }
}
