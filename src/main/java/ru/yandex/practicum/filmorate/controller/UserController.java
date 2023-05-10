package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    public UserController(UserService userService) {
        this.userService = userService;
    }

    private final UserService userService;

    @GetMapping()
    public Collection<User> findAll() {
        log.info("Получен GET-запрос на текущее количество пользователей");
        return userService.findAll();
    }

    @PostMapping()
    public User create(@RequestBody User user) {
        log.info("Получен POST-запрос на добавление пользователя");
        return userService.create(user);
    }

    @PutMapping()
    public User update(@RequestBody User user) {
        log.info("Получен PUT-запрос на обновление пользователя");
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable int id) {
        log.info("Получен GET-запрос на пользователя с ID={}", id);
        return userService.findUserById(id);
    }

    @DeleteMapping("/{id}")
    public User delete(int id) {
        log.info("Получен DELETE-запрос на удаление пользователя с ID={}", id);
        return userService.delete(id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен PUT-запрос на добавление друга к пользователю c ID = {}", id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен DELETE-запрос на удаление друга пользователю c ID = {}", id);
        userService.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable int id) {
        log.info("Получен GET-запрос на текущее количество друзей пользователя с ID = {}", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен GET-запрос на текущее общее количество друзей пользователя с другим пользователем с ID = {}", otherId);
        return userService.getCommonFriends(id, otherId);
    }
}


