package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {
    private final User user = new User();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserController userController = new UserController(new UserService(userStorage));

    @BeforeEach
    void setUp() {
        user.setEmail("email1@email.ru");
        user.setLogin("Login1");
        user.setName("Name1");
        user.setBirthday(LocalDate.of(1987, 1, 23));
    }

    @Test
    @DisplayName("Проверка создания пользователя, если неправильный или пустой email")
    void shouldThrowExceptionWhenCreateUserWithWrongEmail() {
        user.setEmail(" ");
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userController.create(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception.getMessage());
        assertEquals(0, userController.findAll().size(), "Пользователь не должен быть создан");

        user.setEmail("email1.email.ru");
        BadRequestException exception1 = Assertions.assertThrows(
                BadRequestException.class,
                () -> userController.create(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", exception1.getMessage());
        assertEquals(0, userController.findAll().size(), "Пользователь не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания пользователя, если пустой логин или с пробелом")
    void shouldThrowExceptionWhenCreateUserWithWrongLogin() {
        user.setLogin(" ");
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userController.create(user));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception.getMessage());
        assertEquals(0, userController.findAll().size(), "Пользователь не должен быть создан");

        user.setLogin("Log in 1");
        BadRequestException exception1 = Assertions.assertThrows(
                BadRequestException.class,
                () -> userController.create(user));
        assertEquals("Логин не может быть пустым и содержать пробелы", exception1.getMessage());
        assertEquals(0, userController.findAll().size(), "Пользователь не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания пользователя, если дата рождения в будущем")
    void shouldThrowExceptionWhenCreateUserWithWrongBirthday() {
        user.setBirthday(LocalDate.of(2024, 1, 23));
        BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userController.create(user));
        assertEquals("Дата рождения не может быть в будущем", exception.getMessage());
        assertEquals(0, userController.findAll().size(), "Пользователь не должен быть создан");
    }

    @Test
    @DisplayName("Проверка создания пользователя, если имя пустое")
    void shouldCreateUserWithEmptyName() {
        user.setName("");
        userController.create(user);
        assertEquals("Login1", user.getName(), "При пустом имени должен использоваться логин");

        user.setName(null);
        userController.create(user);
        assertEquals("Login1", user.getName(), "При пустом имени должен использоваться логин");
    }

    @Test
    @DisplayName("Проверка создания пользователя")
    void shouldCreateUser() {
        userController.create(user);
        assertEquals(1, userController.findAll().size(), "Неверное количество пользователей");
    }

}
