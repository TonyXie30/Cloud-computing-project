package com.example.operator.config;

import com.example.operator.pojo.PgsqlReaderReconciler;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.Operator;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
public class OperatorConfig {

    @Bean
    public ConcurrentLinkedQueue<Map<String, Object>> changeQueue() {
        return new ConcurrentLinkedQueue<>();
    }

    @Bean
    public KubernetesClient kubernetesClient() {
        return new DefaultKubernetesClient();
    }

    @Bean
    public PgsqlReaderReconciler pgsqlReaderReconciler(ConcurrentLinkedQueue<Map<String, Object>> changeQueue, KubernetesClient kubernetesClient) {
        return new PgsqlReaderReconciler(changeQueue, kubernetesClient);
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    @SuppressWarnings("rawtypes")
    public Operator operator(List<Reconciler> controllers) {
        Operator operator = new Operator();
        controllers.forEach(operator::register);
        return operator;
    }
}
