package com.highload.socialnetwork.controller;

import com.highload.socialnetwork.model.UserPost;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/app/post")
@RestController
@RequiredArgsConstructor
public class PostApi {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpMessagingTemplate messagingTemplate;
    private final RabbitTemplate rabbitTemplate;

    @MessageMapping("/app/post/create")
    @SendTo("/topic")
    @PostMapping("/create")
    public ResponseEntity<UserPost> createPost(@RequestParam String text, @RequestParam String authorUserId) {
        var query = "INSERT INTO posts (id, text, author_user_id) VALUES (nextval('post_id_seq'), :text, :authorUserId)";
        Map<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("authorUserId", authorUserId);
        jdbcTemplate.update(query, params);
        UserPost userPost = new UserPost();
        userPost.setText(text);
        userPost.setAuthorUserId(authorUserId);
        return ResponseEntity.ok(userPost);
    }

    @Cacheable(value = "postCache")
    @GetMapping("/feed")
    public List<UserPost> getPosts() {
        var query = "select * from posts";
        List<UserPost> userPosts = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(UserPost.class));
        return userPosts;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserPost> getPost(@PathVariable Long id) {
        var query = "select * from posts where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        UserPost userPost = jdbcTemplate.queryForObject(query, params, new BeanPropertyRowMapper<>(UserPost.class));
        return ResponseEntity.ok(userPost);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Void> updatePost(@PathVariable Long id, @RequestParam String text, @RequestParam String authorUserId) {
        var query = "update posts set text = :text, author_user_id = :authorUserId where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("text", text);
        params.put("authorUserId", authorUserId);
        jdbcTemplate.update(query, params);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        var query = "delete from posts where id = :id";
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        jdbcTemplate.update(query, params);
        return ResponseEntity.ok().build();
    }
}

