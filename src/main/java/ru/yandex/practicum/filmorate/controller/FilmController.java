package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmValidator filmValidator = new FilmValidator();

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping()
    public Collection<Film> findAll() {
        log.info("Получен GET-запрос на текущий список фильмов");
        return filmService.findAll();
    }

    @PostMapping()
    public Film create(@RequestBody Film film) {
        log.info("Получен POST-запрос на добавление фильма");
        filmValidator.throwIfNotValid(film);
        return filmService.create(film);
    }

    @PutMapping()
    public Film update(@RequestBody Film film) {
        log.info("Получен PUT-запрос на обновление фильма");
        filmValidator.throwIfNotValid(film);
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable int id) {
        log.info("Получен GET-запрос на фильм с ID={}", id);
        return filmService.findById(id);
    }

    @DeleteMapping("/{id}")
    public Film delete(int id) {
        log.info("Получен DELETE-запрос на удаление фильма с ID={}", id);
        return filmService.delete(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен PUT-запрос на добавления лайка к фильму");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен DELETE-запрос на удаления лайка к фильму");
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

}
