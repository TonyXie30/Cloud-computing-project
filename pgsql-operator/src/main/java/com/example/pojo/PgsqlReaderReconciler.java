package com.example.pojo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@ControllerConfiguration
public class PgsqlReaderReconciler implements Reconciler<PgsqlReader> {

    @Value("${database.table}")
    private String table;

    public PgsqlReaderReconciler(JdbcTemplate jdbcTemplate) {
    }

    @Override
    public UpdateControl<PgsqlReader> reconcile(PgsqlReader resource, Context<PgsqlReader> context) {
        PgsqlReaderSpec.DBProperties primaryDb = resource.getSpec().getPrimary();
        List<PgsqlReaderSpec.DBProperties> secondaryDbs = resource.getSpec().getSecondary();

        // 连接主数据库
        DataSource primaryDataSource = createDataSource(primaryDb);
        JdbcTemplate primaryJdbcTemplate = new JdbcTemplate(primaryDataSource);

        // 获取主数据库的 User 表数据
        String sql = "SELECT * FROM " + table;
        List<User> primaryUsers = primaryJdbcTemplate.query(sql, new UserRowMapper());

        for (PgsqlReaderSpec.DBProperties secondaryDb : secondaryDbs) {
            // 连接次要数据库
            DataSource secondaryDataSource = createDataSource(secondaryDb);
            JdbcTemplate secondaryJdbcTemplate = new JdbcTemplate(secondaryDataSource);

            // 同步次要数据库的 User 表
            for (User user : primaryUsers) {
                sql = "INSERT INTO public.user (id, username) VALUES (?, ?) ON CONFLICT (id) DO UPDATE SET name = EXCLUDED.name";
                secondaryJdbcTemplate.update(sql, user.getId(), user.getUsername());
            }
        }

        return UpdateControl.noUpdate();
    }

    private DataSource createDataSource(PgsqlReaderSpec.DBProperties db) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db.getUrl());
        config.setUsername(db.getUsername());
        config.setPassword(db.getPassword());
        return new HikariDataSource(config);
    }

}

class User {
    private Long id;
    private String username;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    // Getters and Setters
}

class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setUsername(rs.getString("username"));
        return user;
    }
    
}
