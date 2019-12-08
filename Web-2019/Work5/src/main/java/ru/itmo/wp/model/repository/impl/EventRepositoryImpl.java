package ru.itmo.wp.model.repository.impl;

import ru.itmo.wp.model.domain.Event;
import ru.itmo.wp.model.repository.EventRepository;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class EventRepositoryImpl extends BasicRepositoryImpl<Event> implements EventRepository {

    @Override
    public void save(Event event) {
        save(List.of(
                Map.entry("userId", event.getUserId()),
                Map.entry("type", event.getType().name())), event);
    }

    Event toEntity(ResultSetMetaData metaData, ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        Event event = new Event();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
            switch (metaData.getColumnName(i)) {
                case "id":
                    event.setId(resultSet.getLong(i));
                    break;
                case "userId":
                    event.setUserId(resultSet.getLong(i));
                    break;
                case "creationTime":
                    event.setCreationTime(resultSet.getTimestamp(i));
                    break;
                case "type":
                    event.setType(Event.Type.valueOf(resultSet.getString(i)));
                    break;
                default:
                    // No operations.
            }
        }

        return event;
    }

    String getSimpleName() {
        return Event.class.getSimpleName();
    }
}
