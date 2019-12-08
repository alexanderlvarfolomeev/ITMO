package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.User;
import ru.itmo.wp.model.repository.UserRepository;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl extends BasicRepositoryImpl<User> implements UserRepository {

    @Override
    public User findByLogin(String login) {
        return find("SELECT * FROM User WHERE login=?",
                List.of(login));
    }

    @Override
    public User findByEmail(String email) {
        return find("SELECT * FROM User WHERE email=?", List.of(email));
    }

    @Override
    public User findByLoginOrEmailAndPasswordSha(String loginOrEmail, String passwordSha) {
        return find("SELECT * FROM User WHERE ( login=? OR email=?) AND passwordSha=?",
                List.of(loginOrEmail, loginOrEmail, passwordSha));
    }

    @Override
    public List<User> findAll() {
        return findAll("SELECT * FROM User ORDER BY id DESC", List.of());
    }

    @Override
    public List<User> findAllById() {
        return findAll("SELECT * FROM User ORDER BY id", List.of());
    }

    User toEntity(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        User user = new User();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    user.setId(resultSet.getLong(i));
                    break;
                case "login":
                    user.setLogin(resultSet.getString(i));
                    break;
                case "creationTime":
                    user.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "email":
                    user.setEmail(resultSet.getString(i));
                    break;
                case "admin":
                    user.setAdmin(resultSet.getBoolean(i));
                    break;
                default:
                    // No operations.
            }
        }

        return user;
    }

    @Override
    public void changeAdmin(long userId, boolean admin) {
        change("UPDATE User SET admin=? WHERE User.id=?",
                List.of(admin, userId));
    }

    @Override
    public void save(User user, String passwordSha) {
        save(List.of(
                Map.entry("login", user.getLogin()),
                Map.entry("email", user.getEmail()),
                Map.entry("passwordSha", passwordSha),
                Map.entry("admin", user.isAdmin())), user);
    }

    String getSimpleName() {
        return User.class.getSimpleName();
    }
}
