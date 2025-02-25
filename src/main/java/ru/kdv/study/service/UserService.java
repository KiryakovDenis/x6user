package ru.kdv.study.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kdv.study.exception.BadRequestException;
import ru.kdv.study.model.User;
import ru.kdv.study.repository.UserRepository;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final String REGEX_EMAIL_PATTERN_RFC5322 = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Exception.class)
    public User createUser(final User user) {
        validate(user);
        return userRepository.insert(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public User update(final User user) {
        validate(user);
        return userRepository.update(user);
    }

    @Transactional(readOnly = true)
    public User getById(final Long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public Boolean userExist(final Long id) {
        return userRepository.userExist(id);
    }

    private void validate(final User user) {
        if (!StringUtils.hasText(user.getName())) {
            throw BadRequestException.create("Поле name должно быть заполнено.");
        }

        if (!StringUtils.hasText(user.getEmail())) {
            throw BadRequestException.create("Поле email должно быть заполнено.");
        }

        if (!emailIsValid(user.getEmail(), REGEX_EMAIL_PATTERN_RFC5322)) {
            throw BadRequestException.create("Поле email заполнено некорректно.");
        }
    }

    private boolean emailIsValid(String email, String regex) {
        return Pattern.compile(regex)
                .matcher(email)
                .matches();
    }
}
