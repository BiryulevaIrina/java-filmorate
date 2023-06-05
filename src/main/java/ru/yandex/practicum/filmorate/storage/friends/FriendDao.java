package ru.yandex.practicum.filmorate.storage.friends;

import ru.yandex.practicum.filmorate.model.Friend;

import java.util.List;

public interface FriendDao {

    public void addFriend(int userId, int friendsId, boolean status);

    public void deleteFriend(int userId, int friendsId);

    public List<Friend> getFriends(int userId);
}
