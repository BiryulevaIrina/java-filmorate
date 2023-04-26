package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Integer, User> users = new HashMap<>();

    private final UserValidator userValidator = new UserValidator();

    private int id;

    @GetMapping()
    public List<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(this.users.values());
    }

    @PostMapping()
    public User create(@RequestBody User user) throws BadRequestException {
        ++id;
        userValidator.throwIfNotValid(user);
        checkUserName(user);
        user.setId(id);
        users.put(id, user);
        log.debug("Зарегистрирован новый пользователь" + user.getName() + user.getEmail());
        return user;
    }

    @PutMapping()
    public User update(@RequestBody User user) throws NotFoundException, BadRequestException {
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

    private void checkUserName(@RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}


