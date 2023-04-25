package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.BadRequestException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public boolean throwIfNotValid(User user) {
        LocalDate date = LocalDate.now();
        if (user.getEmail().isBlank() || (!user.getEmail().contains("@"))) {
            throw new BadRequestException("Электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new BadRequestException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getBirthday().isAfter(date)) {
            throw new BadRequestException("Дата рождения не может быть в будущем");
        }
        return false;
    }
}
