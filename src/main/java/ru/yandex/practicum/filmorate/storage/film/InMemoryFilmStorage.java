package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private final FilmValidator filmValidator = new FilmValidator();

    private int id;

    @Override
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @Override
    public Film create(Film film) {
        ++id;
        filmValidator.throwIfNotValid(film);
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм" + film.getName());
        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким идентификатором нет в базе");
        }
        filmValidator.throwIfNotValid(film);
        id = film.getId();
        films.put(id, film);
        log.debug("Обновлены данные о фильме" + film.getName());
        return film;
    }

    @Override
    public Film findFilmById(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Не найден фильм с ID = " + filmId);
        }
        return films.get(filmId);
    }

    @Override
    public Film delete(int filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Не найден фильм с ID = " + filmId);
        }
        return films.remove(filmId);
    }

}
