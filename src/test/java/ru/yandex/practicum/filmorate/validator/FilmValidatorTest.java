package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    private final Film film = new Film();
    private final FilmStorage filmStorage = new InMemoryFilmStorage();

    private final FilmController filmController = new FilmController(new FilmService(filmStorage,
            null, null, null, null));

    @BeforeEach
    void setUp() {
        film.setName("Film1");
        film.setDescription("Description Film1");
        film.setReleaseDate(LocalDate.of(2021, 1, 15));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        film.setLikes(new HashSet<>());
        film.setGenres(new HashSet<>(Arrays.asList(new Genre(4, "Триллер"),
                new Genre(6, "Боевик"))));
    }

    @Test
    @DisplayName("Проверка создания фильма, если название пустое")
    void shouldThrowExceptionWhenCreateFilmWithEmptyName() {
        film.setName(" ");
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> filmController.create(film));
        assertEquals("Название фильма не может быть пустым", exception.getMessage());
        assertEquals(0, filmStorage.findAll().size(), "Фильм не должен быть создан");

        film.setName(null);
        BadRequestException exception1 = Assertions.assertThrows(
                BadRequestException.class,
                () -> filmController.create(film));
        assertEquals("Название фильма не может быть пустым", exception1.getMessage());
        assertEquals(0, filmStorage.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания фильма, если описание более 200 символов")
    void shouldThrowExceptionWhenCreateFilmWithTooLongDescription() {
        film.setDescription("Cinema is one of the best types of art and cinematography is considered to be one " +
                "of the rare wonders. It has appeared in the end of the 19th century. Cinema is a combination of " +
                "different types of art: music, theater, literature, painting and else. Every decade has brought " +
                "something new for the cinema. For example, in the 30-s the main genres were musicals, " +
                "gangster stories, mute comedies and horror films.");
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> filmController.create(film));
        assertEquals("Максимальная длина описания фильма — 200 символов", exception.getMessage());
        assertEquals(0, filmStorage.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания фильма, если дата релиза раньше 28 декабря 1895 года")
    void shouldThrowExceptionWhenCreateFilmWithWrongReleaseDate() {
        film.setReleaseDate(LocalDate.of(1895, 12, 1));
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> filmController.create(film));
        assertEquals("Дата релиза — не раньше 28 декабря 1895 года", exception.getMessage());
        assertEquals(0, filmStorage.findAll().size(), "Фильм не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания фильма, если продолжительность фильма отрицательная")
    void shouldThrowExceptionWhenCreateFilmWithNegativeDuration() {
        film.setDuration(-100);
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> filmController.create(film));
        assertEquals("Продолжительность фильма должна быть положительной", exception.getMessage());
        assertEquals(0, filmStorage.findAll().size(), "Фильм не должен быть создан");
    }

}
