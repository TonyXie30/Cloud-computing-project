package com.example.pojo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@ControllerConfiguration
public class PgsqlReaderReconciler implements Reconciler<PgsqlReader> {

    private static final Logger log = LoggerFactory.getLogger(PgsqlReaderReconciler.class);

    private ConcurrentLinkedQueue<Map<String, Object>> changeQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    public PgsqlReaderReconciler(ConcurrentLinkedQueue<Map<String, Object>> changeQueue) {
        this.changeQueue = changeQueue;
    }

    public void addChange(Map<String, Object> change) {
        changeQueue.add(change);
    }

    @Override
    public UpdateControl<PgsqlReader> reconcile(PgsqlReader resource, Context<PgsqlReader> context) {

        log.info("Reconciling PgsqlReaderCustomResource: {}", resource);
        List<PgsqlReaderSpec.DBProperties> secondaryDbs = resource.getSpec().getSecondary();

        // 处理变更记录
        Map<String, Object> changeRecord = changeQueue.poll();
        while (changeRecord != null) {
            log.info("Processing change record: {}", changeRecord);
            String insertSql = "INSERT INTO user (username) VALUES (?)";
            try {
                for (PgsqlReaderSpec.DBProperties db : secondaryDbs) {
                    DataSource dataSource = createDataSource(db);
                    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
                    jdbcTemplate.update(insertSql, changeRecord.get("username"));
                    log.info("Inserted record into database: {}", db.getUrl());
                }
            } catch (Exception e) {
                log.error("Error processing change record: {}", changeRecord, e);
            }

            changeRecord = changeQueue.poll();
        }
        return UpdateControl.noUpdate();
    }

    private DataSource createDataSource(PgsqlReaderSpec.DBProperties db) {
        String username = System.getenv(db.getUsername().substring(2, db.getUsername().length() - 1));
        String password = System.getenv(db.getPassword().substring(2, db.getPassword().length() - 1));
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db.getUrl());
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

}