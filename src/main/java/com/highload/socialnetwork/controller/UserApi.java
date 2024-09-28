package com.highload.socialnetwork.controller;


import static org.springframework.http.ResponseEntity.ok;

import com.highload.socialnetwork.mapper.UserRowMapper;
import com.highload.socialnetwork.model.LoginResponse;
import com.highload.socialnetwork.model.RegisterResponse;
import com.highload.socialnetwork.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RequestMapping("api")
@RestController
public class UserApi {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public UserApi(NamedParameterJdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/users/get/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        var query = "select * from users where id = :id";
        var namedParameters = new MapSqlParameterSource()
                .addValue("id", id);
        var user = jdbcTemplate.queryForObject(query, namedParameters, new UserRowMapper());
        if (user != null) {
            return ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/users/search")
    public ResponseEntity<List<User>> getUsers(@RequestParam String firstName, @RequestParam String secondName) {
        var query = "select * from users where second_name like :secondName || '%' and first_name like :firstName || '%' order by birthdate";

        var namedParameters = new MapSqlParameterSource()
                .addValue("firstName", firstName)
                .addValue("secondName", secondName);
        List<User> users = jdbcTemplate.query(query, namedParameters, new UserRowMapper());
        return ResponseEntity.ok(users);

    }

    @PostMapping("/users/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody User user) {
        var passwordEncoder = new BCryptPasswordEncoder();
        var originalPassword = user.getPassword();
        var encodedPassword = passwordEncoder.encode(originalPassword);

        var userId = UUID.randomUUID();
        user.setId(userId.toString());

        var existingUserQuery = "select count(*) from users where login = :login";
        var count = jdbcTemplate.queryForObject(existingUserQuery, Collections.singletonMap("login", user.getLogin()), Integer.class);

        if (count > 0) {
            return new ResponseEntity<>(new RegisterResponse("Login already exists", null), HttpStatus.CONFLICT);
        }

        var query = "insert into users (id, login, first_name, second_name, birthdate, biography, city, password)" +
                " values (:id, :login, :firstName, :secondName, :birthdate, :biography, :city, :password)";
        var namedParameters = new MapSqlParameterSource()
                .addValue("id", userId)
                .addValue("login", user.getLogin())
                .addValue("firstName", user.getFirstName())
                .addValue("secondName", user.getSecondName())
                .addValue("birthdate", user.getBirthdate())
                .addValue("biography", user.getBiography())
                .addValue("city", user.getCity())
                .addValue("password", encodedPassword);
        jdbcTemplate.update(query, namedParameters);

        var registeredUser = new User();
        registeredUser.setId(user.getId());
        registeredUser.setLogin(user.getLogin());
        registeredUser.setFirstName(user.getFirstName());
        registeredUser.setSecondName(user.getSecondName());
        registeredUser.setBirthdate(user.getBirthdate());
        registeredUser.setBiography(user.getBiography());
        registeredUser.setCity(user.getCity());

        return new ResponseEntity<>(new RegisterResponse("User registered successfully", registeredUser), HttpStatus.OK);
    }

    @PostMapping("/users/login")
    public ResponseEntity<LoginResponse> login(@RequestBody User user) {
        var passwordEncoder = new BCryptPasswordEncoder();
        var query = "select * from users where login = :login";
        var namedParameters = new BeanPropertySqlParameterSource(user);
        List<User> users = jdbcTemplate.query(query, namedParameters, (rs, rowNum) -> {
            User u = new User();
            u.setLogin(rs.getString("login"));
            u.setPassword(rs.getString("password"));
            return u;
        });

        User authenticatedUser = users.isEmpty() ? null : users.get(0);
        if (authenticatedUser == null) {
            return ResponseEntity.badRequest().body(new LoginResponse(null, "Invalid login"));
        }
        if (passwordEncoder.matches(user.getPassword(), authenticatedUser.getPassword())) {
            String token = generateToken(authenticatedUser);
            return ok(new LoginResponse(token, null));
        } else {
            return ResponseEntity.badRequest().body(new LoginResponse(null, "Invalid password"));
        }
    }

    private String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiration = now.plusSeconds(360);
        Date expirationDate = Date.from(expiration);
        return Jwts.builder()
                .setSubject(user.getId())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, "secret-key")
                .compact();
    }
}
