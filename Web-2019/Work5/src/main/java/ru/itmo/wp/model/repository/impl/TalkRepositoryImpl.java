package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Talk;
import ru.itmo.wp.model.repository.TalkRepository;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class TalkRepositoryImpl extends BasicRepositoryImpl<Talk> implements TalkRepository {

    @Override
    public List<Talk> findByUserId(long userId) {
        return findAll("SELECT * FROM Talk WHERE sourceUserId=? OR targetUserId=? ORDER BY creationTime DESC",
                List.of(Map.entry("sourceUserId", userId), Map.entry("targetUserId", userId)));
    }

    @Override
    public void save(Talk talk) {
        save(List.of(
                Map.entry("sourceUserId", talk.getSourceUserId()),
                Map.entry("targetUserId", talk.getTargetUserId()),
                Map.entry("text", talk.getText())), talk);
    }

    Talk toEntity(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Talk talk = new Talk();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    talk.setId(resultSet.getLong(i));
                    break;
                case "sourceUserId":
                    talk.setSourceUserId(resultSet.getLong(i));
                    break;
                case "targetUserId":
                    talk.setTargetUserId(resultSet.getLong(i));
                    break;
                case "text":
                    talk.setText(resultSet.getString(i));
                    break;
                case "creationTime":
                    talk.setCreationTime(resultSet.getTimestamp(i));
                    break;
                default:
                    // No operations.
            }
        }

        return talk;
    }

    String getSimpleName() {
        return Talk.class.getSimpleName();
    }
}
