package com.highload.socialnetwork.mapper;

import com.highload.socialnetwork.model.Users;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersRowMapper implements RowMapper<Users> {
    @Override
    public Users mapRow(ResultSet rs, int rowNum) throws SQLException {
        Users user = new Users();
        user.setId(rs.getString("id"));
        user.setFirstName(rs.getString("first_name"));
        user.setLogin(rs.getString("login"));
        user.setSecondName(rs.getString("second_name"));
//        user.setBirthdate(rs.getDate("birthdate") != null ? rs.getDate("birthdate").toLocalDate() : null);
        user.setBiography(rs.getString("biography") != null ? rs.getString("biography") : null);
        user.setCity(rs.getString("city") != null ? rs.getString("city") : null);

        return user;
    }
}
