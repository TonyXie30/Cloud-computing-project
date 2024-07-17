package com.example.operator.pojo;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.fabric8.kubernetes.api.model.Secret;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.ControllerConfiguration;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@ControllerConfiguration
public class PgsqlReaderReconciler implements Reconciler<PgsqlReader> {

    private static final Logger log = LoggerFactory.getLogger(PgsqlReaderReconciler.class);

    private KubernetesClient kubernetesClient;

    private ConcurrentLinkedQueue<Map<String, Object>> changeQueue = new ConcurrentLinkedQueue<>();

    @Autowired
    public PgsqlReaderReconciler(ConcurrentLinkedQueue<Map<String, Object>> changeQueue, KubernetesClient kubernetesClient) {
        this.changeQueue = changeQueue;
        this.kubernetesClient = kubernetesClient;
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
        String username = getSecretValue(db.getUsername());
        String password = getSecretValue(db.getPassword());
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db.getUrl());
        log.info("Creating datasource for: {}", db.getUrl());
        config.setUsername(username);
        config.setPassword(password);
        return new HikariDataSource(config);
    }

    private String getSecretValue(String secretRef) {
        String[] parts = secretRef.split("/");
        String secretName = parts[0];
        String secretKey = parts[1];

        Secret secret = kubernetesClient.secrets().inNamespace("default").withName(secretName).get();
        if (secret != null) {
            return secret.getData().get(secretKey);
        } else {
            throw new IllegalArgumentException("Secret not found: " + secretRef);
        }
    }

}