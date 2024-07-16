package com.example.config;

import com.example.pojo.PgsqlReaderReconciler;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
public class OperatorConfig {

    @Bean
    public PgsqlReaderReconciler pgsqlReaderReconciler(JdbcTemplate jdbcTemplate) {
        return new PgsqlReaderReconciler(jdbcTemplate);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @SuppressWarnings("rawtypes")
    public Operator operator(List<Reconciler> controllers) {
        Operator operator = new Operator();
        controllers.forEach(operator::register);
        return operator;
    }
}
