package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
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

    @ResponseBody
    @PostMapping()
    public Film create(@RequestBody Film film) {
        ++id;
        if (!filmValidator.getIsValid(film)) {
            log.error("Ошибка: проверьте заполнение полей запроса.");
        }
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм" + film.getName());
        return film;
    }

    @ResponseBody
    @PutMapping()
    public Film update(@RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильма с таким идентификатором нет в базе");
        }
        if (!filmValidator.getIsValid(film)) {
            log.debug("Ошибка: проверьте заполнение полей запроса.");
        }
        id = film.getId();
        films.put(id, film);
        log.debug("Обновлены данные о фильме" + film.getName());
        return film;
    }
}
