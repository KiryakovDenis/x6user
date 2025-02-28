package ru.kdv.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createDate;
}