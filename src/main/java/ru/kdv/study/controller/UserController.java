package ru.kdv.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kdv.study.model.User;
import ru.kdv.study.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @Operation(summary = "Создать пользователя")
    public User createUser(@RequestBody final User user) {
        return userService.createUser(user);
    }

    @PatchMapping
    @Operation(summary = "Обновить пользователя")
    public User updateUser(@RequestBody final User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по Id")
    public User getById(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @PostMapping("user/exist/{id}")
    @Operation(summary = "Проверить наличие пользователя по id")
    public boolean userExists(@PathVariable final Long id) {
        return userService.userExist(id);
    }
}