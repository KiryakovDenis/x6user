package ru.kdv.study.repository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.exception.DataBaseException;
import ru.kdv.study.exception.NoDataFoundException;
import ru.kdv.study.model.User;
import ru.kdv.study.repository.mapper.UserExistMapper;
import ru.kdv.study.repository.mapper.UserMapper;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private static String INSERT = """
            INSERT 
              INTO user_service."user" ("name", email, create_date) 
            VALUES(:name, :email, :create_date)
            RETURNING *
            """;

    private static String UPDATE = """
            UPDATE user_service."user"
               SET name = :name
             WHERE id = :id
            RETURNING *
            """;

    private static String SELECT_BY_ID = """
            SELECT *
              FROM user_service."user"
             WHERE id = :id
            """;

    private static String SELECT_BY_EXIST = """
            SELECT exists(SELECT 1
                            FROM user_service."user" u
                           WHERE u.id = :id) as value
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final UserExistMapper userExistMapper;

    public User insert(final User user) {
        try {
            return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
        } catch (Exception e) {
            throw handleDbExceptionMessage(e, user);
        }
    }

    public User update(final User user) {
        try {
            return jdbcTemplate.queryForObject(UPDATE, userToSql(user), userMapper);
        } catch (Exception e) {
            throw handleDbExceptionMessage(e, user);
        }
    }

    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (EmptyResultDataAccessException e) {
            throw  handleDbExceptionMessage(e, id);
        }
    }

    public Boolean userExist(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_EXIST, new MapSqlParameterSource("id", id), userExistMapper);
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    private MapSqlParameterSource userToSql(final User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", user.getId());
        params.addValue("name", user.getName());
        params.addValue("email", user.getEmail());
        params.addValue("create_date", LocalDateTime.now());

        return params;
    }

    private RuntimeException handleDbExceptionMessage (final Exception e, final User user) throws DataBaseException{
        if (e instanceof EmptyResultDataAccessException) {
            return NoDataFoundException.create(String.format("Пользователь не найден {id = %s}", user.getId()));
        } else if (e.getMessage().contains("user_uk1")) {
            return DataBaseException.create(String.format("Пользователь с электронной почтой \" %s \" - уже существует", user.getEmail()));
        } else {
            return DataBaseException.create(e.getMessage());
        }
    }

    private RuntimeException handleDbExceptionMessage (final Exception e, Long id) throws DataBaseException{
        if (e instanceof EmptyResultDataAccessException) {
            return NoDataFoundException.create(String.format("Пользователь не найден {id = %s}", id));
        } else {
            return DataBaseException.create(e.getMessage());
        }
    }
}