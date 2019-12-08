package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.itmo.wp.form.NoticeCredentials;
import ru.itmo.wp.form.validator.NoticeCreationValidator;
import ru.itmo.wp.service.NoticeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class CreateNoticePage extends Page {
    private final NoticeService noticeService;
    private final NoticeCreationValidator noticeCreationValidator;

    public CreateNoticePage(NoticeService noticeService,
                            NoticeCreationValidator noticeCreationValidator) {
        this.noticeService = noticeService;
        this.noticeCreationValidator = noticeCreationValidator;
    }

    @InitBinder("creationForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(noticeCreationValidator);
    }

    @GetMapping("/createNotice")
    public String createNoticeGet(Model model, HttpSession httpSession) {
        if (getUser(httpSession) == null) {
            putMessage(httpSession, "You should be logged in to have access to this page.");
            return "redirect:/";
        }
        model.addAttribute("creationForm", new NoticeCredentials());
        return "CreateNoticePage";
    }

    @PostMapping("/createNotice")
    public String createNoticePost(@Valid @ModelAttribute("creationForm") NoticeCredentials creationForm,
                           BindingResult bindingResult,
                           HttpSession httpSession) {
        if (getUser(httpSession) == null) {
            putMessage(httpSession, "You should be logged in to have access to create notices.");
            return "redirect:/";
        }
        if (bindingResult.hasErrors()) {
            return "CreateNoticePage";
        }
        noticeService.create(creationForm);
        putMessage(httpSession, "Notice was successfully created!");

        return "redirect:/";
    }
}
