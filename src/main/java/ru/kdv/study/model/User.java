package ru.kdv.study.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class User {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createDate;
}
