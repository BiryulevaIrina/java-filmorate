package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    ;

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public Film findFilmById(int filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public Film delete(int filmId) {
        return filmStorage.delete(filmId);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.findFilmById(filmId);
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = filmStorage.findFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Не найден пользователь с ID = " + userId);
        }
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopularFilms(int count) {
        return findAll().stream()
                .sorted((f1, f2) -> f2.getLikes().size() - f1.getLikes().size())
                .limit(count)
                .collect(Collectors.toList());
    }
}
