package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.util.Objects.requireNonNull;

public class UserWithRolesResultSetExtractor implements ResultSetExtractor<List<User>> {
    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    @Override
    public List<User> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<User> resultUserList = new ArrayList<>();
        if (!rs.next()) {
            return null;
        }
        int rowNum = 1;
        User user = null;
        Set<Role> roles = null;
        do {
            int userId = rs.getInt("id");
            boolean userChanged = user != null && userId != requireNonNull(user.getId());
            if (userChanged) {
                user.setRoles(roles);
                resultUserList.add(user);
            }
            if (user == null || userChanged) {
                user = requireNonNull(ROW_MAPPER.mapRow(rs, rowNum));
                roles = new HashSet<>();
            }
            String roleName = rs.getString("role");
            if (roleName != null) {
                roles.add(Role.valueOf(rs.getString("role")));
            }
            rowNum++;
        } while (rs.next());
        user.setRoles(roles);
        resultUserList.add(user);
        return resultUserList;
    }
}
