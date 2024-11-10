package com.highload.socialnetwork.service;

import com.highload.socialnetwork.mapper.UsersRowMapper;
import com.highload.socialnetwork.model.Users;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataMigrationService {
    private final JdbcTemplate jdbcTemplate;
    private final RedisTemplate<String, Users> redisTemplate;

//    @EventListener(ApplicationReadyEvent.class)
    public void migrateUsersToRedis() {
        String query = "SELECT * FROM users";
        List<Users> users = jdbcTemplate.query(query, new UsersRowMapper());

        for (Users user : users) {
            String cacheKey = "usersCache::" + user.getFirstName() + ", " + user.getSecondName() + "]";
            redisTemplate.opsForValue().set(cacheKey, user);
        }
    }
}
