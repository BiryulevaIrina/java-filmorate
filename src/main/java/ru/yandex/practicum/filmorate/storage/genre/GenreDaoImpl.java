package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;


@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate, GenreMapper genreMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreMapper = genreMapper;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id_genre, name FROM genres ORDER BY id_genre", genreMapper);
    }

    @Override
    public Optional<Genre> findById(int id) {
        return jdbcTemplate.query("SELECT id_genre, name FROM genres WHERE id_genre=?",
                        genreMapper, id)
                .stream().findAny();
    }

    @Override
    public void addGenres(int filmId, Set<Genre> genres) {
        for (Genre genre : genres) {
            jdbcTemplate.update("INSERT INTO films_genres (id_film, id_genre) VALUES(?, ?)",
                    filmId, genre.getId());
        }
    }

    @Override
    public Set<Genre> getGenres(int filmId) {
        List<Genre> genreList = jdbcTemplate.query("SELECT fg.id_genre, g.name FROM films_genres AS fg "
                + "LEFT OUTER JOIN genres AS g ON fg.id_genre = g.id_genre "
                + "WHERE fg.id_film=? ORDER BY g.id_genre ASC", genreMapper, filmId);
        return new HashSet<>(genreList);
    }

    @Override
    public void deleteGenre(int filmId) {
        jdbcTemplate.update("DELETE FROM films_genres WHERE id_film=?", filmId);
    }

    @Override
    public void updateGenres(int filmId, Set<Genre> genres) {
        deleteGenre(filmId);
        addGenres(filmId, genres);
    }

    @Override
    public void load(List<Film> films) {
        String inQuery = String.join(",", Collections.nCopies(films.size(), "?"));

        String mainQuery = "SELECT * FROM genres AS g, films_genres AS fg "
                + "WHERE fg.id_genre = g.id_genre AND fg.id_film IN (" + inQuery + ") ";

        Map<Integer, Film> filmById = films.stream()
                .collect(Collectors.toMap(Film::getId, identity()));

        jdbcTemplate.query(mainQuery, (rs) -> {
            Film film = filmById.get(rs.getInt("id_film"));
            Genre genre = genreMapper.mapRow(rs, 0);
            film.addGenre(genre);
        }, films.stream()
                .map(Film::getId).toArray());
    }
}
