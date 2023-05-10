package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private final UserValidator userValidator = new UserValidator();

    private int id;

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }

    @Override
    public User create(User user) throws BadRequestException {
        ++id;
        userValidator.throwIfNotValid(user);
        checkUserName(user);
        user.setId(id);
        users.put(id, user);
        log.debug("Зарегистрирован новый пользователь" + user.getName() + user.getEmail());
        return user;
    }

    @Override
    public User update(User user) throws BadRequestException, NotFoundException {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователя с таким идентификатором нет в базе");
        }
        userValidator.throwIfNotValid(user);
        checkUserName(user);
        id = user.getId();
        users.put(id, user);
        log.debug("Обновлены данные о пользователе" + user.getName() + user.getEmail());
        return user;
    }

    @Override
    public User findUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с ID = " + userId);
        }
        return users.get(userId);
    }

    @Override
    public User delete(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Не найден пользователь с ID = " + userId);
        }
        for (User user : users.values()) {
            user.addFriend(userId);
        }
        return users.remove(userId);
    }

    @Override
    public void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
