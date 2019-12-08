package ru.itmo.wp.web.page;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.service.ArticleService;
import ru.itmo.wp.model.service.UserService;
import ru.itmo.wp.web.exception.RedirectException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/** @noinspection unused*/
public class ArticlePage {
    private final ArticleService articleService = new ArticleService();

    private void action(HttpServletRequest request, Map<String, Object> view) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "You should be logged in to have access to that page.");
            throw new RedirectException("/index");
        }
    }

    private void create(HttpServletRequest request, Map<String, Object> view) throws ValidationException {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            request.getSession().setAttribute("message", "You should be logged in to post articles");
            throw new RedirectException("/index");
        }
        Article article = new Article();
        article.setUserId(user.getId());
        article.setTitle(request.getParameter("title"));
        article.setText(request.getParameter("text"));
        article.setHidden("on".equals(request.getParameter("hidden")));
        articleService.create(article);
    }
}
