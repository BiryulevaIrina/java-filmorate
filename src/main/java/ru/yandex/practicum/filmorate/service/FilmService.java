package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;
import ru.yandex.practicum.filmorate.storage.like.LikeDao;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingDao;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LikeDao likeDao;
    private final GenreDao genreDao;
    private final MpaRatingDao mpaRatingDao;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeDao likeDao, GenreDao genreDao, MpaRatingDao mpaRatingDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDao = likeDao;
        this.genreDao = genreDao;
        this.mpaRatingDao = mpaRatingDao;
    }

    public List<Film> findAll() {
        List<Film> films = filmStorage.findAll();
        for (Film film : films) {
            film.setGenres(genreDao.getGenres(film.getId()));
            film.setMpa(mpaRatingDao.getByFilmId(film.getId()));
        }
        return films;
    }

    public Film create(Film film) {
        Film newFilm = filmStorage.create(film);
        newFilm.setMpa(mpaRatingDao.getByFilmId(newFilm.getId()));
        genreDao.addGenres(newFilm.getId(), film.getGenres());
        newFilm.setGenres(genreDao.getGenres(newFilm.getId()));
        return newFilm;
    }

    public Film update(Film film) {
        if (findById(film.getId()) == null) {
            throw new NotFoundException("Фильма с идентификатором " + film.getId() + " нет в базе.");
        }
        Film updateFilm = filmStorage.update(film);
        genreDao.updateGenres(updateFilm.getId(), film.getGenres());
        updateFilm.setGenres(genreDao.getGenres(updateFilm.getId()));
        updateFilm.setMpa(mpaRatingDao.getByFilmId(updateFilm.getId()));
        return updateFilm;
    }

    public Film findById(int filmId) {
        Optional<Film> filmOptional = filmStorage.findById(filmId);
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильма с идентификатором " + filmId + " нет в базе.");
        } else {
            Film film = filmOptional.get();
            film.setGenres(genreDao.getGenres(filmId));
            film.setMpa(mpaRatingDao.getByFilmId(filmId));
            return film;
        }
    }

    public Film delete(int filmId) {
        Film film = findById(filmId);
        if (findById(filmId) == null) {
            throw new NotFoundException("Фильма с идентификатором " + filmId + " нет в базе.");
        }
        filmStorage.delete(filmId);
        return film;
    }

    public void addLike(int filmId, int userId) {
        if (findById(filmId) != null) {
            if (userStorage.findById(userId).isPresent()) {
                likeDao.addLike(filmId, userId);
            } else {
                throw new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе.");
            }
        } else {
            throw new NotFoundException("Фильма с идентификатором " + filmId + " нет в базе.");
        }
    }

    public void deleteLike(int filmId, int userId) {
        if (findById(filmId) != null) {
            if (userStorage.findById(userId).isPresent()) {
                likeDao.deleteLike(filmId, userId);
            } else {
                throw new NotFoundException("Ошибка при указании ID = " + userId);
            }
        } else {
            throw new NotFoundException("Ошибка при указании ID = " + filmId);
        }
    }

    public List<Film> getPopularFilms(int count) {
        if (count >= 1) {
            List<Film> popularFilms = likeDao.getPopularFilms(count);
            for (Film popularFilm : popularFilms) {
                popularFilm.setGenres(genreDao.getGenres(popularFilm.getId()));
                popularFilm.setMpa(mpaRatingDao.getByFilmId(popularFilm.getId()));
            }
            return popularFilms;
        }
        throw new BadRequestException("Ошибка запроса, указано некорректное количество фильмов (меньше 1)" + count);
    }
}
