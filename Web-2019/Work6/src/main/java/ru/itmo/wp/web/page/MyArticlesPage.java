package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @noinspection unused
 */
public class MyArticlesPage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "You should be logged in to have access to that page.");
            throw new RedirectException("/index");
        }
        view.put("articles", articleService.findAllByUserId(user.getId()));
    }

    private void changeHidden(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        long articleId = Long.parseLong(request.getParameter("articleId"));
        try {
            articleService.validateHiddenChange(user, articleId);
            boolean hidden = Boolean.parseBoolean(request.getParameter("hidden"));
            articleService.changeHidden(hidden, articleId);
            view.put("hidden", hidden);
        } catch (ValidationException e) {
            view.put("message", e.getMessage());
        }
    }
}
