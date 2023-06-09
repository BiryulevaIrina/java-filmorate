package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    @Override
    public List<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return new ArrayList<>(this.films.values());
    }

    @Override
    public Film create(Film film) {
        ++id;
        film.setId(id);
        films.put(id, film);
        log.info("Добавлен новый фильм" + film.getName());
        return film;
    }

    @Override
    public Film update(Film film) throws NotFoundException {
        id = film.getId();
        films.put(id, film);
        log.debug("Обновлены данные о фильме" + film.getName());
        return film;
    }

    @Override
    public Optional<Film> findById(int id) {
        return Optional.ofNullable(films.get(id));
    }

    @Override
    public void delete(int id) {
        films.remove(id);
    }

}
