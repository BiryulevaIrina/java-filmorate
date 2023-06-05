package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpaRating.MpaRatingDao;

import java.util.List;

@Service
public class MpaRatingService {
    private final MpaRatingDao mpaRatingDao;

    @Autowired
    public MpaRatingService(MpaRatingDao mpaRatingDao) {
        this.mpaRatingDao = mpaRatingDao;
    }

    public List<Mpa> findAll() {
        return mpaRatingDao.findAll();
    }

    public Mpa findById(int id) {
        return mpaRatingDao.findById(id)
                .orElseThrow(() -> new NotFoundException("MPA рейтинга с идентификатором " + id + " нет в базе."));
    }
}
