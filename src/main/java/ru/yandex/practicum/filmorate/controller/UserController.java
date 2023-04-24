package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    @ResponseBody
    @PostMapping()
    public User create(@RequestBody User user) {
        ++id;
        if (!userValidator.getIsValid(user)) {
            log.error("Ошибка: проверьте заполнение полей запроса.");
        }
        user.setId(id);
        users.put(id, user);
        log.debug("Зарегистрирован новый пользователь" + user.getName() + user.getEmail());
        return user;
    }

    @ResponseBody
    @PutMapping()
    public User update(@RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Фильма с таким идентификатором нет в базе");
        }
        if (!userValidator.getIsValid(user)) {
            log.error("Ошибка: проверьте заполнение полей запроса.");
        }
        id = user.getId();
        users.put(id, user);
        log.debug("Обновлены данные о пользователе" + user.getName() + user.getEmail());
        return user;
    }
}


