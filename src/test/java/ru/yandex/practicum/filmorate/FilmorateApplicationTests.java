package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {

    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final User user = new User();
    private final Film film = new Film();

    @BeforeEach
    void setUp() {
        user.setEmail("email1@email.ru");
        user.setLogin("Login1");
        user.setName("Name1");
        user.setBirthday(LocalDate.of(2002, 3, 12));

        userStorage.create(user);

        film.setName("Film1");
        film.setDescription("Description Film1");
        film.setReleaseDate(LocalDate.of(2021, 1, 15));
        film.setDuration(100);
        film.setMpa(new Mpa(1, "G"));
        film.setLikes(new HashSet<>());
        film.setGenres(new HashSet<>(Arrays.asList(new Genre(4, "Триллер"),
                new Genre(6, "Боевик"))));

        filmStorage.create(film);
    }

    @Test
    void contextLoads() {
    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.findById(user.getId());

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", user.getId())
                );
    }

    @Test
    public void testUpdateUser() {
        user.setEmail("email_other@email.ru");
        user.setBirthday(LocalDate.of(2002, 3, 12));

        Optional<User> userOptional = Optional.ofNullable(userStorage.update(user));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", user.getId())
                                .hasFieldOrPropertyWithValue("email", "email_other@email.ru")
                );
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.findAll();

        assertThat(users).hasSize(1);
        assertThat(users).contains(user);
    }

    @Test
    public void testDeleteUser() {
        userStorage.delete(user.getId());
        List<User> users = userStorage.findAll();

        assertThat(users).hasSize(0);
    }

    @Test
    public void testFindFilmById() {

        Optional<Film> filmOptional = filmStorage.findById(film.getId());

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film)
                                .hasFieldOrPropertyWithValue("id", film.getId())
                );
    }

    @Test
    public void testUpdateFilm() {
        film.setName("Film1_update");
        film.setDescription("Other Description Film1");
        film.setReleaseDate(LocalDate.of(2021, 1, 15));
        film.setDuration(101);

        Optional<Film> filmOptional = Optional.ofNullable(filmStorage.update(film));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", film.getId())
                                .hasFieldOrPropertyWithValue("name", "Film1_update")
                );
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmStorage.findAll();

        assertThat(films).hasSize(1);
    }

    @Test
    public void testDeleteFilm() {
        filmStorage.delete(film.getId());
        List<Film> films = filmStorage.findAll();

        assertThat(films).hasSize(0);
    }

    @AfterEach
    void cleanUpEach() {
        userStorage.delete(user.getId());
        filmStorage.delete(film.getId());
    }

}
