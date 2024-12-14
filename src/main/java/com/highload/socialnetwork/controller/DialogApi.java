package com.highload.socialnetwork.controller;

import com.highload.socialnetwork.model.Dialog;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("v1/dialog")
@RestController
@RequiredArgsConstructor
public class DialogApi {
    private final NamedParameterJdbcTemplate jdbcTemplate;


    @GetMapping("/{userId}/list")
    public List<Dialog> getDialogs(@PathVariable String userId) {
        var query = "select * from dialogs where user_id = :userId";
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        List<Dialog> dialogs = jdbcTemplate.query(query, params, new BeanPropertyRowMapper<>(Dialog.class));
        return dialogs;
    }

    @PostMapping("/dialog/{user_id}/send")
    public ResponseEntity<Void> createPost(@RequestBody Dialog dialog) {
        var query = "INSERT INTO dialogs (text, user_id) VALUES (:text, :userId)";
        Map<String, Object> params = new HashMap<>();
        params.put("text", dialog.getText());
        params.put("userId", dialog.getUserId());
        jdbcTemplate.update(query, params);

        return ResponseEntity.ok().build();
    }
}
