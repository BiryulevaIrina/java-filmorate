package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    private final Film film = new Film();
    private final FilmController filmController = new FilmController();

    @BeforeEach
    void setUp() {
        film.setName("Film1");
        film.setDescription("Description Film1");
        film.setReleaseDate(LocalDate.of(2022, 3, 12));
        film.setDuration(100);
    }

    @Test
    @DisplayName("Проверка создания фильма, если название пустое")
    void shouldThrowExceptionWhenCreateFilmWithEmptyName() {
        film.setName(" ");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
        assertEquals(0, filmController.findAll().size(), "Фильм не должен быть создан");

        film.setName(null);
        ValidationException exception1 = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Название фильма не может быть пустым", exception1.getMessage());
        assertEquals(0, filmController.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания фильма, если описание более 200 символов")
    void shouldThrowExceptionWhenCreateFilmWithTooLongDescription() {
        film.setDescription("Cinema is one of the best types of art and cinematography is considered to be one " +
                "of the rare wonders. It has appeared in the end of the 19th century. Cinema is a combination of " +
                "different types of art: music, theater, literature, painting and else. Every decade has brought " +
                "something new for the cinema. For example, in the 30-s the main genres were musicals, " +
                "gangster stories, mute comedies and horror films.");
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Максимальная длина описания фильма — 200 символов", exception.getMessage());
        assertEquals(0, filmController.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания фильма, если дата релиза раньше 28 декабря 1895 года")
    void shouldThrowExceptionWhenCreateFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 01));
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
        assertEquals(0, filmController.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания фильма, если продолжительность фильма отрицательная")
    void shouldThrowExceptionWhenCreateFilmWithNegativeDuration() {
        film.setDuration(-100);
        ValidationException exception = Assertions.assertThrows(
                ValidationException.class,
                () -> filmController.create(film));
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
        assertEquals(0, filmController.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void shouldCreateUser() {
        filmController.create(film);
        assertEquals(1, filmController.findAll().size(), "Неверное количество пользователей");
    }

}