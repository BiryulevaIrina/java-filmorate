package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDao;

import java.util.List;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getGenres() {
        return genreDao.findAll();
    }

    public Genre getGenreById(int id) {
        return genreDao.findById(id)
                .orElseThrow(() -> new NotFoundException("Жанра с идентификатором " + id + " нет в базе."));
    }
}
