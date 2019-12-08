package ru.itmo.wp.model.service;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.exception.ValidationException;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.UserRepository;
import ru.itmo.wp.model.repository.impl.ArticleRepositoryImpl;
import ru.itmo.wp.model.repository.impl.UserRepositoryImpl;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ArticleService {
    private final ArticleRepository articleRepository = new ArticleRepositoryImpl();

    private void validateArticle(Article article) throws ValidationException {
        if (Strings.isNullOrEmpty(article.getTitle())) {
            throw new ValidationException("Title is required");
        }

        if (Strings.isNullOrEmpty(article.getText())) {
            throw new ValidationException("Text is required");
        }
    }

    public void validateHiddenChange(User user, long articleId) throws ValidationException {
        Article article = find(articleId);

        if (article.getUserId() != user.getId()) {
            throw new ValidationException("Article properties can be edited only by author");
        }
    }

    public Article find(long id) {
        return articleRepository.find(id);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Article> findAllVisible() {
        return articleRepository.findAllVisible();
    }
    public List<Article> findAllByUserId(long userId) {
        return articleRepository.findAllByUserId(userId);
    }

    public void changeHidden(boolean hidden, long articleId) {
        articleRepository.changeHidden(hidden, articleId);
    }

    public void create(Article article) throws ValidationException {
        validateArticle(article);
        articleRepository.save(article);
    }
}
