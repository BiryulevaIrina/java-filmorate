package ru.yandex.practicum.filmorate.storage.like;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Like;

import java.util.List;

public interface LikeDao {

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    List<Film> getPopularFilms(int count);
}
