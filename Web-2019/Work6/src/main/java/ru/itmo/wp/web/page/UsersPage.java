package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
public class UsersPage {
    private final UserService userService = new UserService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            userService.updateAdmin(user);
        }
        view.put("user", user);
    }

    private void findAll(HttpServletRequest request, Map<String, Object> view) {
        view.put("users", userService.findAll());
    }

    private void findUser(HttpServletRequest request, Map<String, Object> view) {
        view.put("user", userService.find(Long.parseLong(request.getParameter("userId"))));
    }

    private void changeAdmin(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        try {
            userService.validateAdmin(user);
            boolean admin = Boolean.parseBoolean(request.getParameter("admin"));
            long userId = Long.parseLong(request.getParameter("userId"));
            userService.changeAdmin(userId, admin);
            view.put("admin", admin);
            if (user.getId() == userId) {
                userService.updateAdmin(user);
                view.put("user", user);
            }
        } catch (ValidationException e) {
            view.put("message", e.getMessage());
        }

    }
}
