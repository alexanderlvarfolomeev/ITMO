package ru.itmo.wp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.DisabilityCredentials;
import ru.itmo.wp.form.validator.DisabilityChangingValidator;
import ru.itmo.wp.service.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class UsersPage extends Page {
    private final UserService userService;
    private final DisabilityChangingValidator disabilityChangingValidator;

    public UsersPage(UserService userService, DisabilityChangingValidator disabilityChangingValidator) {
        this.userService = userService;
        this.disabilityChangingValidator = disabilityChangingValidator;
    }

    @InitBinder("disabilityForm")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(disabilityChangingValidator);
    }

    @GetMapping("/users/all")
    public String users(Model model) {
        model.addAttribute("users", userService.findAll());
        return "UsersPage";
    }

    @PostMapping("/users/all")
    public String change(@Valid @ModelAttribute("disabilityForm") DisabilityCredentials disabilityForm,
                         BindingResult bindingResult, HttpSession httpSession) {
        User user = getUser(httpSession);
        if (user == null) {
            putMessage(httpSession, "You should be logged in to have access to this operation.");
            return "redirect:/";
        }
        if (!bindingResult.hasErrors()) {
            userService.setDisability(disabilityForm);
        }
        if (user.getId() == disabilityForm.getId()) {
            unsetUser(httpSession);
            putMessage(httpSession, "You are now disabled.");
        }
        return "redirect:/users/all";
    }
}
