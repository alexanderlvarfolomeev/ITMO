package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.EventService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
public class LogoutPage extends Page {
    private final EventService eventService = new EventService();

    @Override
    void action() {
        User user = getUser();
        if (user != null) {
            eventService.save(user, Event.Type.LOGOUT);
            request.getSession().removeAttribute("user");

            setMessage("Good bye. Hope to see you soon!");
        }
        throw new RedirectException("/index");
    }
}
