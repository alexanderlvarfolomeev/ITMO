package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Article;
import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.ArticleRepository;
import ru.itmo.wp.model.repository.UserRepository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ArticleRepositoryImpl extends BasicRepositoryImpl<Article> implements ArticleRepository {

    @Override
    public List<Article> findAll() {
        return findAll("SELECT * FROM Article ORDER BY creationTime DESC", List.of());
    }

    @Override
    public List<Article> findAllVisible() {
        return findAll("SELECT * FROM Article WHERE hidden=? ORDER BY creationTime DESC",
                List.of(false));
    }

    @Override
    public List<Article> findAllByUserId(long userId) {
        return findAll("SELECT * FROM Article WHERE userId=? ORDER BY creationTime DESC",
                List.of(userId));
    }

    Article toEntity(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Article article = new Article();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    article.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    article.setUserId(resultSet.getLong(i));
                    break;
                case "title":
                    article.setTitle(resultSet.getString(i));
                    break;
                case "text":
                    article.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    article.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "hidden":
                    article.setHidden(resultSet.getBoolean(i));
                    break;
                default:
                    // No operations.
            }
        }

        return article;
    }

    @Override
    public void save(Article article) {
        save(List.of(
                Map.entry("userId", article.getUserId()),
                Map.entry("title", article.getTitle()),
                Map.entry("text", article.getText()),
                Map.entry("hidden", article.isHidden())), article);
    }

    @Override
    public void changeHidden(boolean hidden, long articleId) {
        change("UPDATE Article SET hidden=? WHERE Article.id=?",
                List.of(hidden, articleId));
    }

    String getSimpleName() {
        return Article.class.getSimpleName();
    }
}
