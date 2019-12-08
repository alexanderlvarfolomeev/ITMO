package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.domain.Post;
import ru.itmo.wp.domain.Role;
import ru.itmo.wp.domain.Tag;
import ru.itmo.wp.form.PostForm;
import ru.itmo.wp.security.AnyRole;
import ru.itmo.wp.service.TagService;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class WritePostPage extends Page {
    private final UserService userService;
    private final TagService tagService;

    public WritePostPage(UserService userService, TagService tagService) {
        this.userService = userService;
        this.tagService = tagService;
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @GetMapping("/writePost")
    public String writePostGet(Model model) {
        model.addAttribute("post", new PostForm());
        return "WritePostPage";
    }

    @AnyRole({Role.Name.WRITER, Role.Name.ADMIN})
    @PostMapping("/writePost")
    public String writePostPost(@Valid @ModelAttribute("post") PostForm postForm,
                                BindingResult bindingResult,
                                HttpSession httpSession) {
        if (bindingResult.hasErrors()) {
            return "WritePostPage";
        }
        Post post = new Post();
        post.setText(postForm.getText());
        post.setTitle(postForm.getTitle());
        Set<Tag> tags = Arrays.stream(postForm.getTags().split("\\s"))
                .filter(s -> !s.isEmpty()).map(s -> {
                    Tag tag = tagService.findByName(s);
                    if (tag == null) tag = new Tag(s);
                    return tag;
                }).collect(Collectors.toSet());
        post.setTags(tags);
        userService.writePost(getUser(httpSession), post);
        putMessage(httpSession, "You published new post");

        return "redirect:/posts";
    }
}
