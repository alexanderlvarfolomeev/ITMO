package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.service.TalkService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @noinspection unused
 */
public class TalksPage extends Page {
    private final TalkService talkService = new TalkService();
    private final UserService userService = new UserService();

    @Override
    void before(HttpServletRequest request, Map<String, Object> view) {
        super.before(request, view);

        User user = getUser();
        if (user == null) {
            setMessage("You should be logged in to have access to that page.");
            throw new RedirectException("/index");
        }
    }

    private void action(Map<String, Object> view) {
        User user = getUser();
        List<Talk> talks = talkService.findByUser(user);
        view.put("talks", talks);
        view.put("users", userService.findAll());
    }

    private void send(Map<String, Object> view) {
        User user = getUser();
        Talk talk = new Talk();
        talk.setSourceUserId(user.getId());
        talk.setTargetUserId(Long.parseLong(request.getParameter("targetId")));
        talk.setText(request.getParameter("text"));
        talkService.save(talk);
        throw new RedirectException("/talks");
    }
}
