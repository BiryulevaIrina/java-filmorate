package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;
    private final UserValidator userValidator = new UserValidator();

    @GetMapping()
    public List<User> getUsers() {
        log.info("Получен GET-запрос на текущий список пользователей");
        return userService.getUsers();
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя");
        userValidator.throwIfNotValid(user);
        return userService.create(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя");
        userValidator.throwIfNotValid(user);
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("Получен GET-запрос на пользователя с ID={}", id);
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public User delete(int id) {
        log.info("Получен DELETE-запрос на удаление пользователя с ID={}", id);
        return userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT-запрос на добавление друга с ID = {} к пользователю c ID = {}", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE-запрос на удаление друга с ID = {} к пользователю c ID = {}", friendId, id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("Получен GET-запрос на текущее количество друзей пользователя с ID = {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен GET-запрос на текущее общее количество друзей пользователя с ID = {} " +
                "с другим пользователем с ID = {}", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }
}


