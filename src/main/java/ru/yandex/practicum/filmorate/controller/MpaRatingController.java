package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @Autowired
    public MpaRatingController(MpaRatingService mpaRatingService) {
        this.mpaRatingService = mpaRatingService;
    }

    @GetMapping()
    public List<Mpa> findAll() {
        log.info("Получен GET-запрос на текущий список MPA рейтингов");
        return mpaRatingService.findAll();
    }

    @GetMapping("/{id}")
    public Mpa findById(@PathVariable int id) {
        log.info("Получен GET-запрос на MPA рейтинг с ID={}", id);
        return mpaRatingService.findById(id);
    }
}
