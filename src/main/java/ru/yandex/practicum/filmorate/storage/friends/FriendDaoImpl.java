package ru.yandex.practicum.filmorate.storage.friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

@Component
public class FriendDaoImpl implements FriendDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFriend(int userId, int friendId, boolean status) {
        jdbcTemplate.update("INSERT INTO friends VALUES(?, ?, ?)", userId, friendId, status);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM friends WHERE id_user=? AND id_friend=?", userId, friendId);
    }

    @Override
    public List<Friend> getFriends(int userId) {
        return jdbcTemplate.query("SELECT id_friend FROM friends WHERE id_user=? GROUP BY id_friend ORDER BY id_friend",
                new FriendMapper(), userId);
    }

}
