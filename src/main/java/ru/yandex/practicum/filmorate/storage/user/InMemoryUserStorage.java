package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int id;

    @Override
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(this.users.values());
    }

    @Override
    public User create(User user) throws BadRequestException {
        ++id;
        user.setId(id);
        users.put(id, user);
        log.debug("Зарегистрирован новый пользователь" + user.getName() + user.getEmail());
        return user;
    }

    @Override
    public User update(User user) throws BadRequestException, NotFoundException {
        id = user.getId();
        users.put(id, user);
        log.debug("Обновлены данные о пользователе" + user.getName() + user.getEmail());
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User delete(int userId) {
        return users.remove(userId);
    }
}
