package ru.yandex.practicum.filmorate.storage.mpaRating;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaRatingDao {

    List<Mpa> findAll();

    Optional<Mpa> findById(int id);

    Mpa getByFilmId(int filmId);
}
