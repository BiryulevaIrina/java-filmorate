package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

@Component
@Primary
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private int id;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", userMapper);
    }

    @Override
    public User create(User user) {
        user.setId(++id);
        jdbcTemplate.update("INSERT INTO users VALUES(?, ?, ?, ?, ?)", user.getId(), user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name=?, birthday=? "
                        + "WHERE id_user=?", user.getEmail(),
                user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        return jdbcTemplate.query("SELECT id_user, email, login, name, birthday FROM users WHERE id_user=?",
                userMapper, id).stream().findAny();
    }

    @Override
    public void delete(int userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id_user=?", userId);
    }
}
