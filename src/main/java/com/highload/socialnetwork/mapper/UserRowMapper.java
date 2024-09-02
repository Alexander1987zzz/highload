package com.highload.socialnetwork.mapper;

import com.highload.socialnetwork.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLogin(rs.getString("login"));
        user.setSecondName(rs.getString("second_name"));
        user.setBirthdate(rs.getDate("birthdate").toLocalDate());
        user.setBiography(rs.getString("biography"));
        user.setCity(rs.getString("city"));

        return user;
    }
}
