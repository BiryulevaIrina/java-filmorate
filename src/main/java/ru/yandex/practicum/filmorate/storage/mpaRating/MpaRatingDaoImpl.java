package ru.yandex.practicum.filmorate.storage.mpaRating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Component
public class MpaRatingDaoImpl implements MpaRatingDao {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRatingMapper mpaRatingMapper;

    @Autowired
    public MpaRatingDaoImpl(JdbcTemplate jdbcTemplate, MpaRatingMapper mpaRatingMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaRatingMapper = mpaRatingMapper;
    }

    @Override
    public List<Mpa> findAll() {
        return jdbcTemplate.query("SELECT id_mpa_rating, name FROM mpa_ratings ORDER BY id_mpa_rating",
                mpaRatingMapper);
    }

    @Override
    public Optional<Mpa> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM mpa_ratings WHERE id_mpa_rating=?",
                        mpaRatingMapper, id)
                .stream().findAny();
    }

    @Override
    public Mpa getByFilmId(int filmId) {
        return jdbcTemplate.queryForObject("SELECT m.* FROM mpa_ratings AS m INNER JOIN films AS f "
                + "ON f.id_mpa_rating = m.id_mpa_rating WHERE id_film=?", mpaRatingMapper, filmId);
    }

    @Override
    public void load(List<Film> films) {
        String inQuery = String.join(",", Collections.nCopies(films.size(), "?"));

        String mainQuery = "SELECT * FROM mpa_ratings AS m, films AS f "
                + "WHERE f.id_mpa_rating = m.id_mpa_rating AND f.id_film IN (" + inQuery + ") ";

        Map<Integer, Film> filmById = films.stream()
                .collect(Collectors.toMap(Film::getId, identity()));

        jdbcTemplate.query(mainQuery, (rs) -> {
            Film film = filmById.get(rs.getInt("id_film"));
            Mpa mpa = mpaRatingMapper.mapRow(rs, 0);
            film.setMpa(mpa);
        }, films.stream()
                .map(Film::getId).toArray());
    }

}
