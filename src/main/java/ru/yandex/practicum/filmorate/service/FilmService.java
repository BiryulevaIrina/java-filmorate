package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        if (findAll().contains(film)) {
            throw new BadRequestException("Фильм " + film.getName() + " уже есть в базе с ID = " + film.getId());
        }
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        if (findById(film.getId()) == null) {
            throw new NotFoundException("Фильма с идентификатором " + film.getId() + " нет в базе.");
        }
        return filmStorage.update(film);
    }

    public Film findById(int filmId) {
        return filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с идентификатором " + filmId + " нет в базе."));
    }

    public Film delete(int filmId) {
        if (findById(filmId) == null) {
            throw new NotFoundException("Фильма с идентификатором " + filmId + " нет в базе.");
        }
        return filmStorage.delete(filmId);
    }

    public void addLike(int filmId, int userId) {
        Film film = findById(filmId);
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = findById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Ошибка при указании ID = " + userId);
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
