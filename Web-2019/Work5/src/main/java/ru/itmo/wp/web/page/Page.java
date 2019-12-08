package ru.itmo.wp.web.page;

import com.google.common.base.Strings;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
class Page {
    private final UserService userService = new UserService();
    HttpServletRequest request;

    void before(HttpServletRequest request, Map<String, Object> view) {
        this.request = request;
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            view.put("user", user);
        }

        view.put("userCount", userService.findCount());

        String message = (String) request.getSession().getAttribute("message");
        if (!Strings.isNullOrEmpty(message)) {
            view.put("message", message);
            removeMessage();
        }
    }

    void action() {
        // No operations.
    }

    void after(HttpServletRequest request, Map<String, Object> view) {
        // No operations.
    }

    void setMessage(String message) {
        request.getSession().setAttribute("message", message);
    }

    private void removeMessage() {
        request.getSession().removeAttribute("message");
    }

    void setUser(User user) {
        request.getSession().setAttribute("user", user);
    }

    User getUser() {
        return (User) request.getSession().getAttribute("user");
    }
}
