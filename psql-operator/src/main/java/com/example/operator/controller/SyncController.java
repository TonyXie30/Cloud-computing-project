package com.example.operator.controller;

import com.example.operator.pojo.PgsqlReader;
import com.example.operator.pojo.PgsqlReaderReconciler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/sync")
public class SyncController {

    @Autowired
    private PgsqlReaderReconciler pgsqlReaderReconciler;

    @Value("${TARGET_TABLE:user}")
    private String targetTable;

    @PostMapping
    public ResponseEntity<Void> syncUser(@RequestBody Map<String, Object> payload) {
        // 在这里处理同步逻辑
        // payload 中包含 table, operation, record 等信息
        // 从 payload 获取表名、操作类型和记录数据
        String table = (String) payload.get("table");
        String operation = (String) payload.get("operation");
        Map<String, Object> record = (Map<String, Object>) payload.get("record");

        // 这里只处理 User 表的变动
        if (targetTable.equals(table) || targetTable.contains(table) || table.contains(targetTable)) {
            // 调用 reconcile 方法执行同步逻辑
            pgsqlReaderReconciler.addChange(record);
            PgsqlReader resource = new PgsqlReader();
            // 在此处设置 resource 的属性，模拟 reconcile 方法接收的资源
            pgsqlReaderReconciler.reconcile(resource, null);
        }
        return ResponseEntity.ok().build();
    }
}

