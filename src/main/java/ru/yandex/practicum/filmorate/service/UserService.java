package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        checkUserName(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        checkUserName(user);
        if (findById(user.getId()) == null) {
            throw new NotFoundException("Пользователя с идентификатором " + user.getId() + " нет в базе.");
        }
        return userStorage.update(user);
    }

    public User findById(int userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе."));
    }

    public User delete(int userId) {
        return userStorage.delete(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе."));
    }

    public void addFriend(int userId, int friendsId) {
        User user = findById(userId);
        User friend = findById(friendsId);
        user.addFriend(friendsId);
        friend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendsId) {
        User user = findById(userId);
        User friend = findById(friendsId);
        user.getFriends().remove(friendsId);
        friend.getFriends().remove(userId);
    }

    public List<User> getFriends(int userId) {
        User user = findById(userId);
        Set<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(findById(id));
        }
        return friends.stream()
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>(getFriends(id));
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }

    public void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
