package ru.yandex.practicum.filmorate.storage.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
<<<<<<< HEAD
import ru.yandex.practicum.filmorate.model.Like;
=======
>>>>>>> 5633de2 (fix: правка согласно код-стайлу)
import ru.yandex.practicum.filmorate.storage.film.FilmMapper;

import java.util.List;

@Component
public class LikeDaoImpl implements LikeDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(int filmId, int userId) {
        jdbcTemplate.update("INSERT INTO likes (id_film, id_user) VALUES(?, ?)", filmId, userId);
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE id_film=? AND id_user=?", filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(int count) {
        return jdbcTemplate.query("SELECT f.id_film, f.name, f.description, f.release_date, f.duration, " + "f.id_mpa_rating FROM films AS f LEFT JOIN likes AS l ON f.id_film = l.id_film " + "GROUP BY f.id_film ORDER BY COUNT(l.id_user) DESC LIMIT ?", new FilmMapper(), count);
    }
}
