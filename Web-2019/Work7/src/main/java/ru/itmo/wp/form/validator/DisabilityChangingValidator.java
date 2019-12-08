package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.controller.IndexPage;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.form.DisabilityCredentials;
import ru.itmo.wp.form.UserCredentials;
import ru.itmo.wp.service.UserService;

@Component
public class DisabilityChangingValidator implements Validator {
    private final UserService userService;

    public DisabilityChangingValidator(UserService userService) {
        this.userService = userService;
    }

    public boolean supports(Class<?> clazz) {
        return DisabilityCredentials.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {}
}
