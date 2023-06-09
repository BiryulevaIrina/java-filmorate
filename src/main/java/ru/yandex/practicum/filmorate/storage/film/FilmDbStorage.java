package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
@Primary
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmMapper filmMapper;

    private int id;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmMapper filmMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.filmMapper = filmMapper;
    }

    @Override
    public List<Film> findAll() {
        return jdbcTemplate.query("SELECT * FROM films", filmMapper);
    }

    @Override
    public Film create(Film film) {
        film.setId(++id);
        jdbcTemplate.update("INSERT INTO films VALUES(?, ?, ?, ?, ?, ?)", film.getId(), film.getName(),
                film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId());
        return film;
    }

    @Override
    public Film update(Film film) {
        jdbcTemplate.update("UPDATE films SET name=?, description=?, release_date=?, duration=?, "
                        + "id_mpa_rating=? WHERE id_film=?", film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), film.getId());
        return film;
    }

    @Override
    public Optional<Film> findById(int id) {
        return jdbcTemplate.query("SELECT id_film, name, description, release_date, duration, id_mpa_rating "
                        + "FROM films WHERE id_film=?", filmMapper, id)
                .stream().findAny();
    }

    @Override
    public void delete(int filmId) {
        jdbcTemplate.update("DELETE FROM films WHERE id_film=?", filmId);
    }

}