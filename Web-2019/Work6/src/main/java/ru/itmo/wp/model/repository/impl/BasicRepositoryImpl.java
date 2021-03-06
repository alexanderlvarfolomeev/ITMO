package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.database.DatabaseUtils;
import ru.itmo.wp.model.domain.Entity;
import ru.itmo.wp.model.exception.RepositoryException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

abstract class BasicRepositoryImpl<T extends Entity> {
    private final DataSource DATA_SOURCE = DatabaseUtils.getDataSource();

    public T find(long id) {
        return find("SELECT * FROM " + getSimpleName() + " WHERE id=?", List.of(id));
    }

    T find(String statementPart, List<Object> fields) {
        try {
            return findAll(statementPart, fields).get(0);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    List<T> findAll(String statementString, List<Object> fields) {
        List<T> entities = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(statementString)) {
                setStatement(fields, statement);
                try (ResultSet resultSet = statement.executeQuery()) {
                    T entity;
                    while ((entity = toEntity(statement.getMetaData(), resultSet)) != null) {
                        entities.add(entity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't find " + getSimpleName() + '.', e);
        }
        return entities;
    }

    void save(List<Map.Entry<String, Object>> fieldsMap, T entity) {
        String format = generateInsertStatement(fieldsMap);
        List<Object> fields = fieldsMap.stream().map(Map.Entry::getValue).collect(Collectors.toList());
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(format, Statement.RETURN_GENERATED_KEYS)) {
                setStatement(fields, statement);
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't save " + getSimpleName() + '.');
                } else {
                    ResultSet generatedKeys = statement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        entity.setId(generatedKeys.getLong(1));
                        entity.setCreationTime(find(entity.getId()).getCreationTime());
                    } else {
                        throw new RepositoryException("Can't save " + getSimpleName() + " [no autogenerated fields].");
                    }
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save " + getSimpleName() + '.', e);
        }
    }

    void change(String statementPart, List<Object> fields) {
        try (Connection connection = DATA_SOURCE.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(statementPart, Statement.RETURN_GENERATED_KEYS)) {
                setStatement(fields, statement);
                if (statement.executeUpdate() != 1) {
                    throw new RepositoryException("Can't save " + getSimpleName() + '.');
                }
            }
        } catch (SQLException e) {
            throw new RepositoryException("Can't save " + getSimpleName() + '.', e);
        }
    }

    private String generateInsertStatement(List<Map.Entry<String, Object>> fields) {
        StringBuilder builder = new StringBuilder("INSERT INTO `");
        builder.append(getSimpleName());
        builder.append("` (`");
        for (Map.Entry<String, Object> field : fields) {
            builder.append(field.getKey());
            builder.append("`, `");
        }
        builder.append("creationTime`) VALUES (");
        for (int i = 0; i < fields.size(); ++i) {
            builder.append("?, ");
        }
        builder.append("NOW())");
        return builder.toString();
    }

    private void setStatement(List<Object> fields, PreparedStatement statement) throws SQLException {
        for (int i = 0; i < fields.size(); ++i) {
            Object field = fields.get(i);
            Class<?> clazz = field.getClass();
            if (clazz.equals(Long.class)) {
                statement.setLong(i + 1, (Long) field);
            } else if (clazz.equals(Boolean.class)) {
                statement.setBoolean(i + 1, (Boolean) field);
            } else {
                statement.setString(i + 1, (String) field);
            }
        }
    }

    abstract T toEntity(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException;

    abstract String getSimpleName();
}
