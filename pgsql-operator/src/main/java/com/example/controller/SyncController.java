package com.example.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @PostMapping
    public ResponseEntity<Void> syncUser(@RequestBody Map<String, Object> payload) {
        // 在这里处理同步逻辑
        // payload 中包含 table, operation, record 等信息
        return ResponseEntity.ok().build();
    }
}

