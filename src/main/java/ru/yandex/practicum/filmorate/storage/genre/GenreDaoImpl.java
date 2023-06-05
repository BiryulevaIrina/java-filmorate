package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id_genre, name FROM genres ORDER BY id_genre", new GenreMapper());
    }

    @Override
    public Optional<Genre> findById(int id) {
        Genre genre = jdbcTemplate.query("SELECT id_genre, name FROM genres WHERE id_genre=?",
                        new GenreMapper(), id)
                .stream().findAny().orElse(null);
        if (genre == null) {
            return Optional.empty();
        }
        return Optional.of(genre);
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
        return new HashSet<>(jdbcTemplate.query("SELECT fg.id_genre, g.name FROM films_genres AS fg "
                + "LEFT OUTER JOIN genres AS g ON fg.id_genre = g.id_genre "
                + "WHERE fg.id_film=? ORDER BY g.id_genre", new GenreMapper(), filmId));
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

}
