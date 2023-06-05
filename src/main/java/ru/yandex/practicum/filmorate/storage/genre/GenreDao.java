package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface GenreDao {

    List<Genre> findAll();

    Optional<Genre> findById(int id);

    void addGenres(int filmId, Set<Genre> genres);

    Set<Genre> getGenres(int filmId);

    void deleteGenre(int filmId);

    void updateGenres(int filmId, Set<Genre> genres);

}
