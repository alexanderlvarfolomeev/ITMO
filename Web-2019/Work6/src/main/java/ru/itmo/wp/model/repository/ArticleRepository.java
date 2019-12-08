package ru.itmo.wp.model.repository;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;

import java.util.List;

public interface ArticleRepository {
    Article find(long id);
    List<Article> findAll();
    List<Article> findAllVisible();
    List<Article> findAllByUserId(long userId);
    void save(Article article);

    void changeHidden(boolean hidden, long articleId);
}
