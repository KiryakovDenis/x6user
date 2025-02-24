package ru.kdv.study.x6user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kdv.study.exception.BadRequestException;
import ru.kdv.study.exception.DataBaseException;
import ru.kdv.study.model.User;
import ru.kdv.study.repository.UserRepository;
import ru.kdv.study.service.UserService;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private final User validUser = new User(
            1L,
            "Тестовый пользователь",
            "testUser@mail.ru",
            LocalDateTime.now()
    );

    @Test
    @DisplayName("Успешное создание пользователя")
    public void insertUserSuccess() {
        Mockito.when(userRepository.insert(validUser)).thenReturn(validUser);

        User result = userService.createUser(validUser);

        assertThat(result).isEqualTo(validUser);
        verify(userRepository).insert(validUser);
    }

    @Test
    @DisplayName("Успешное обновление пользователя")
    public void updateUserSuccess() {
        Mockito.when(userRepository.update(validUser)).thenReturn(validUser);

        User result = userService.update(validUser);

        assertThat(result).isEqualTo(validUser);
        verify(userRepository).update(validUser);
    }

    @Test
    @DisplayName("Ошибка при попытке создать пользователя с неуникальным email")
    public void insertNonUniqueEmail() {
        Mockito.when(userRepository.insert(validUser))
                .thenThrow(DataBaseException.create(
                        String.format("Пользователь с электронной почтой \" %s \" - уже существует", validUser.getEmail()))
                );

        DataBaseException dataBaseException = assertThrows(DataBaseException.class, () -> {
            userService.createUser(validUser);
        });

        verify(userRepository).insert(validUser);
    }

    @Test
    @DisplayName("Ошибка при попытке обновить пользователя задав неуникальнй email")
    public void updateNonUniqueEmail() {
        Mockito.when(userRepository.update(validUser))
                .thenThrow(DataBaseException.create(
                        String.format("Пользователь с электронной почтой \" %s \" - уже существует", validUser.getEmail()))
                );

        DataBaseException dataBaseException = assertThrows(DataBaseException.class, () -> {
            userService.update(validUser);
        });

        verify(userRepository).update(validUser);
    }


    private final User userNameIsNull = new User(
            1L,
            null,
            "testUser@mail.ru",
            LocalDateTime.now()
    );

    @Test
    @DisplayName("Валидация null в качестве имени пользователя")
    public void validateUserNameIsNull() {
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
            userService.createUser(userNameIsNull);
        });
    }

    private final User userNameIsEmpty = new User(
            1L,
            "",
            "testUser@mail.ru",
            LocalDateTime.now()
    );

    @Test
    @DisplayName("Валидация пустой строки в качестве имени пользователя")
    public void validateUserNameIsEmpty() {
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
            userService.createUser(userNameIsEmpty);
        });
    }


    private final User userBadEmail = new User(
            1L,
            "Тестовый пользователь",
            "testUsermail.ru",
            LocalDateTime.now()
    );

    @Test
    @DisplayName("Валидация неправильно заполненного email")
    public void validateUserBadEmail() {
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> {
            userService.createUser(userBadEmail);
        });
    }
}