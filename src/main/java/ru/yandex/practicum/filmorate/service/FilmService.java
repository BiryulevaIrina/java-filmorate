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
    public FilmService(FilmStorage filmStorage, UserStorage userStorage, LikeDao likeDao,
                       GenreDao genreDao, MpaRatingDao mpaRatingDao) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.likeDao = likeDao;
        this.genreDao = genreDao;
        this.mpaRatingDao = mpaRatingDao;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.findAll();
        genreDao.load(films);
        mpaRatingDao.load(films);
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
        filmStorage.findById(film.getId())
                .orElseThrow(() -> new NotFoundException("Фильма с id " + film.getId() + " нет в базе."));
        Film updateFilm = filmStorage.update(film);
        genreDao.updateGenres(updateFilm.getId(), film.getGenres());
        updateFilm.setGenres(genreDao.getGenres(updateFilm.getId()));
        updateFilm.setMpa(mpaRatingDao.getByFilmId(updateFilm.getId()));
        return updateFilm;
    }

    public Film getFilmById(int filmId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с id " + filmId + " нет в базе."));

        film.setGenres(genreDao.getGenres(filmId));
        film.setMpa(mpaRatingDao.getByFilmId(filmId));

        return film;
    }

    public Film delete(int filmId) {
        Film film = filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с id " + filmId + " нет в базе."));
        filmStorage.delete(filmId);
        return film;
    }

    public void addLike(int filmId, int userId) {
        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с id " + filmId + " нет в базе."));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе."));
        likeDao.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        filmStorage.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Фильма с id " + filmId + " нет в базе."));
        userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователя с идентификатором " + userId + " нет в базе."));
        likeDao.addLike(filmId, userId);
    }

    public List<Film> getPopularFilms(int count) {
        if (count >= 1) {
            List<Film> popularFilms = likeDao.getPopularFilms(count);
            genreDao.load(popularFilms);
            mpaRatingDao.load(popularFilms);
            return popularFilms;
        }
        throw new BadRequestException("Ошибка запроса, указано некорректное количество фильмов (меньше 1)" + count);
    }

}

