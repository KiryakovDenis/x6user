package ru.kdv.study.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class User implements Serializable {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createDate;
}