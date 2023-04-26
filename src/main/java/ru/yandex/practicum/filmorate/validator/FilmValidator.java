package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {

    public void throwIfNotValid(Film film) {
        LocalDate date = LocalDate.parse("1895-12-28");
        if ((film.getName() == null) || film.getName().isBlank()) {
            throw new BadRequestException("Название фильма не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new BadRequestException("Максимальная длина описания фильма — 200 символов");
        }
        if (film.getReleaseDate().isBefore(date)) {
            throw new BadRequestException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            throw new BadRequestException("Продолжительность фильма должна быть положительной");
        }
    }
}
