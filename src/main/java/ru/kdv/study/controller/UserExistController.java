package ru.kdv.study.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kdv.study.service.UserService;

@RestController
@RequestMapping("/api/v1/exist")
@RequiredArgsConstructor
public class UserExistController {

    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Проверить наличие пользователя по id")
    public Boolean userExists(@PathVariable final Long id) {
        return userService.userExist(id);
    }
}
