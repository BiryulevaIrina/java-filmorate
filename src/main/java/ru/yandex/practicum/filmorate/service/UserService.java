package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public User findUserById(int userId) {
        return userStorage.findUserById(userId);
    }

    public User delete(int userId) {
        return userStorage.delete(userId);
    }

    public void addFriend(int userId, int friendsId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendsId);
        user.addFriend(friendsId);
        friend.addFriend(userId);
    }

    public void deleteFriend(int userId, int friendsId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendsId);
        user.getFriends().remove(friendsId);
        friend.getFriends().remove(userId);
    }

    public Collection<User> getFriends(int userId) {
        User user = userStorage.findUserById(userId);
        Set<User> friends = new HashSet<>();
        for (Integer id : user.getFriends()) {
            friends.add(userStorage.findUserById(id));
        }
        return friends.stream()
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        Set<User> commonFriends = new HashSet<>(getFriends(id));
        commonFriends.retainAll(getFriends(otherId));
        return commonFriends;
    }
}
