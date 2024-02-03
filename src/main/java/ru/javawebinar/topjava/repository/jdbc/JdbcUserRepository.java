package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static ru.javawebinar.topjava.util.ValidationUtil.validateObject;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validateObject(user);
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            List<Role> userRoles = new ArrayList<>(user.getRoles());
            batchInsertRoles(user.getId(), userRoles);
        } else {
            int userFieldsUpdated = namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource);
            List<Role> newRoles = new ArrayList<>(user.getRoles());
            int userId = requireNonNull(user.getId());
            List<Role> existingRoles = getRoles(userId);
            List<Role> toAddRoles = getMissedItems(newRoles, existingRoles);
            List<Role> toDeleteRoles = getMissedItems(existingRoles, newRoles);
            boolean rolesNotChanged = true;
            if (!toAddRoles.isEmpty()) {
                rolesNotChanged = noUpdatesApplied(batchInsertRoles(userId, toAddRoles));
            }
            if (!toDeleteRoles.isEmpty()) {
                rolesNotChanged = noUpdatesApplied(batchDeleteRoles(userId, toDeleteRoles)) && rolesNotChanged;
            }
            if (userFieldsUpdated == 0 && rolesNotChanged) {
                return null;
            }
        }
        return user;
    }

    private List<Role> getRoles(int userId) {
        return jdbcTemplate.query("SELECT role FROM user_role WHERE user_id=?",
                (rs, rowNum) -> Role.valueOf(rs.getString("role")),
                userId);
    }

    private <T> List<T> getMissedItems(List<T> toCheckItems, List<T> toCheckList) {
        List<T> missedItems = new ArrayList<>();
        for (T itemToCheck : toCheckItems) {
            if (!toCheckList.contains(itemToCheck)) {
                missedItems.add(itemToCheck);
            }
        }
        return missedItems;
    }

    private int[] batchInsertRoles(int userId, List<Role> rolesList) {
        String sqlCommand = "INSERT INTO user_role (user_id, role) VALUES (?, ?)";
        return rolesBatchUpdate(sqlCommand, userId, rolesList);
    }

    private int[] batchDeleteRoles(int userId, List<Role> rolesList) {
        String sqlCommand = "DELETE FROM user_role WHERE (user_id=? AND role=?)";
        return rolesBatchUpdate(sqlCommand, userId, rolesList);
    }

    private int[] rolesBatchUpdate(String sqlCommand, int userId, List<Role> rolesList) {
        return jdbcTemplate.batchUpdate(sqlCommand,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, userId);
                        ps.setString(2, rolesList.get(i).name());
                    }

                    @Override
                    public int getBatchSize() {
                        return rolesList.size();
                    }
                });
    }

    private boolean noUpdatesApplied(int[] updatesCount) {
        return Arrays.stream(updatesCount).noneMatch((count) -> count > 0);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u " +
                "LEFT JOIN user_role r ON r.user_id = u.id " +
                "WHERE u.id=?", new UserWithRolesResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT u.*, r.role FROM users u " +
                "LEFT JOIN user_role r ON r.user_id = u.id " +
                "WHERE u.email=?", new UserWithRolesResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT u.*, r.role FROM users u " +
                "LEFT JOIN user_role r ON r.user_id = u.id " +
                "ORDER BY u.name, u.email", new UserWithRolesResultSetExtractor());
    }
}
