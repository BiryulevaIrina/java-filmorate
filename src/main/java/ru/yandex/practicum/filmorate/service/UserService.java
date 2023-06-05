package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Friend;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friends.FriendDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;
    private final FriendDao friendsDao;

    @Autowired
    public UserService(UserStorage userStorage, FriendDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    public List<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        userNameVerification(user);
        return userStorage.create(user);
    }

    public User update(User user) {
        userNameVerification(user);
        if (findById(user.getId()) != null) {
            return userStorage.update(user);
        } else {
            throw new NotFoundException("Пользователя с идентификатором " + user.getId() + " нет в базе.");
        }
    }

    public User findById(int userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе."));
    }

    public User delete(int userId) {
        User user = findById(userId);
        if (findById(userId) == null) {
            throw new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе.");
        }
        userStorage.delete(userId);
        return user;
    }

    public void addFriend(int userId, int friendId) {
        if (findById(friendId) == null) {
            throw new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе.");
        }
        friendsDao.addFriend(userId, friendId, true);
    }

    public void deleteFriend(int userId, int friendId) {
        if (findById(friendId) == null) {
            throw new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе.");
        }
        friendsDao.deleteFriend(userId, friendId);
    }

    public List<User> getFriends(int userId) {
        Set<User> friends = new HashSet<>();
        for (Friend friend : friendsDao.getFriends(userId)) {
            User user = findById(friend.getIdFriend());
            friends.add(user);
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

    public void userNameVerification(User user) {
        if (user.getName() == null || user.getName().isBlank() || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
    }
}
