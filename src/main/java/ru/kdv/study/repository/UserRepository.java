package ru.kdv.study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.kdv.study.exception.DataBaseException;
import ru.kdv.study.exception.NoDataFoundException;
import ru.kdv.study.model.User;
import ru.kdv.study.repository.mapper.UserMapper;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private static final String INSERT = """
            INSERT INTO user_service."user" ("name", email)
            VALUES(:name, :email)
            RETURNING *
            """;

    private static final String UPDATE = """
            UPDATE user_service."user"
               SET name = :name,
                   email = :email
             WHERE id = :id
            RETURNING *
            """;

    private static final String SELECT_BY_ID = """
            SELECT *
              FROM user_service."user"
             WHERE id = :id
            """;

    private static final String SELECT_BY_EXIST = """
            SELECT exists(SELECT 1
                            FROM user_service."user" u
                           WHERE u.id = :id) as value
            """;

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

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

    public boolean userExist(Long id) {
        try {
            return Boolean.TRUE.equals(jdbcTemplate.queryForObject(SELECT_BY_EXIST, new MapSqlParameterSource("id", id), Boolean.class));
        } catch (Exception e) {
            throw DataBaseException.create(e.getMessage());
        }
    }

    private MapSqlParameterSource userToSql(final User user) {
        MapSqlParameterSource params = new MapSqlParameterSource();

        params.addValue("id", user.getId());
        params.addValue("name", user.getName());
        params.addValue("email", user.getEmail());

        return params;
    }

    private RuntimeException handleDbExceptionMessage (final Exception e, final User user) throws DataBaseException{
        if (e instanceof EmptyResultDataAccessException) {
            return NoDataFoundException.create(String.format("Пользователь не найден {id = %s}", user.getId()));
        } if (e.getMessage().contains("user_uk1")) {
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