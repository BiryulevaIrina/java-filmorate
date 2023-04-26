package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmValidator filmValidator = new FilmValidator();

    private int id;

    @GetMapping()
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(this.films.values());
    }

    @PostMapping()
    public Film create(@RequestBody Film film) throws BadRequestException {
        ++id;
        filmValidator.throwIfNotValid(film);
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм" + film.getName());
        return film;
    }

    @PutMapping()
    public Film update(@RequestBody Film film) throws NotFoundException, BadRequestException {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильма с таким идентификатором нет в базе");
        }
        filmValidator.throwIfNotValid(film);
        id = film.getId();
        films.put(id, film);
        log.debug("Обновлены данные о фильме" + film.getName());
        return film;
    }
}
