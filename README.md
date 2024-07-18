# EventEase: Seamless Event Booking System

Team Members:
- Student 1: Xie Shangru, T0933655.
- Student 2: Hon Hao Yuan, T0933705.
- Student 3: Yang Mingchuan, T0933721.
- Student 4: Liu Shufan, T0933702.

## 1. Project Overview

### 1.1 Project Description

EventEase is an innovative event booking system designed to simplify and streamline the process of organizing and attending events. Our application leverages the power of Kubernetes to ensure scalable and reliable deployment. We integrate Grafana for real-time monitoring and observability, and Grafana Loki for efficient log aggregation and querying.

A key feature of EventEase is our custom operator, which ensures seamless synchronization of distributed databases, maintaining robust data consistency across multiple instances. This allows for a reliable and efficient booking experience, even in a highly distributed environment.

By utilizing these cutting-edge technologies, EventEase provides a seamless, scalable, and reliable platform for event booking, capable of handling high traffic and ensuring data integrity.

### 1.2 System Architecture

EventEase is a cutting-edge event booking system divided into two main components: the front-end and a suite of microservices. The system leverages Kubernetes for scalable and reliable deployment, complemented by robust monitoring and logging solutions using Prometheus, Grafana, and Grafana Loki. A custom operator ensures seamless synchronization of distributed databases, maintaining data consistency across multiple instances.

#### Components

##### 1. Front-End

- **Deployment**: The front-end application is deployed using a Kubernetes Deployment.
- **LoadBalancer Service**: A LoadBalancer service is set up as the entry point for the front-end application, ensuring it is accessible externally.

##### 2. Microservices

- **Components**: The microservices include auth, redis, main, user, and verify.
- **Deployment**: Each microservice is deployed using a Kubernetes Deployment.
- **StatefulSets**: For the main, user, and verify services, we use StatefulSets to manage PostgreSQL databases, ensuring database decoupling and persistence. The auth and redis microservices do not use StatefulSets.
- **Init Containers**: For the main, user, and verify deployments, init containers are configured to wait for the respective StatefulSets (PostgreSQL databases) to be fully operational before starting the main containers.
- **Services**: Each microservice and StatefulSet has an associated Kubernetes Service for intercommunication within the cluster. Services are either of type ClusterIP or LoadBalancer, depending on their accessibility needs.

##### 3. Monitoring and Logging

- **Prometheus**:
  - **Sidecar Exporters**: We have set up sidecar containers (exporters) for all StatefulSets (main, user, verify using PostgreSQL exporters) and the Redis service. These exporters extract metrics in a Prometheus-recognizable format.
  - **Metrics Collection**: Prometheus collects these metrics and forwards them to Grafana for visualization.
- **Grafana**:
  - **Visualization**: Grafana visualizes the metrics collected by Prometheus, providing a clear overview of the system's health and performance.
- **Grafana Loki**:
  - **Log Aggregation**: Promtail and Fluent Bit are distributed across the cluster to collect logs from containers. These logs are then processed by Grafana Loki and integrated into the Grafana dashboard for unified visualization and analysis.

##### 4. Custom Operator

- **Database Synchronization**: The custom operator ensures seamless synchronization of distributed databases, maintaining robust data consistency across multiple instances.
- **Mechanism**:
  - Triggers within the databases initiate changes.
  - A sidecar container detects these triggers and sends the updates to the configured operator.
  - The operator then synchronizes these updates with secondary databases.

##### 5. Tracing

- **Zipkin**: Zipkin is used to gather timing data, helping to troubleshoot latency issues in the services by providing detailed tracing information.

#### System Architecture Diagrams

#### Overall System Architecture
![Overall System Architecture](./Architecture%20Diagram/Overview.png)

#### Microservices Architecture
![Microservices Architecture](./Architecture%20Diagram/Microservices.png)

#### Monitoring and Logging Setup
![Monitoring and Logging Setup](./Architecture%20Diagram/Monitoring%20and%20Logging%20Setup.png)

#### Database Synchronization Flow
![Database Synchronization Flow](./Architecture%20Diagram/Data%20Synchronization%20.png)

#### Tracing Setup
![Tracing Setup](./Architecture%20Diagram/Tracing.png)

### 1.3 Key Components
- **Front-End**: Deployed using a Kubernetes Deployment with a LoadBalancer service for external access.
- **Microservices**: Includes auth, redis, main, user, and verify services, deployed using Kubernetes Deployments and StatefulSets for database management.
- **Monitoring and Logging**: Utilizes Prometheus, Grafana, and Grafana Loki for metrics collection, visualization, and log aggregation.
- **Custom Operator**: Ensures seamless synchronization of distributed databases for data consistency.
- **Tracing**: Uses Zipkin for troubleshooting latency issues in services.

## 2. Environment Setup

### 2.1 Prerequisites
List all the prerequisites needed to set up the environment.
- **Kubernetes**: v1.18+
- **Helm**: v3.5+
- **Docker**: v19.03+

### 2.2 Installation
Step-by-step guide to setting up the development environment.

1. **Step 1**: Setup AWS EKS.
    - Follow the cluster setup steps provided by TA Mao Yancan.
    - **Note**: When creating the node groups, set the node instance type from `t3.medium` to `t3.large` to ensure enough pods can be created.
2. **Step 2**: Install Helm.
    - Follow this [guide](https://helm.sh/docs/intro/install/) to install helm on Linux.

## 3. Application Deployment

Detailed steps to deploy the application.

1. **Step 1**: Deploy Prometheus, Grafana and Grafana Loki.
    ```bash
    # We first cd into the k8s-manifest that contains all the necessary yaml file
    cd k8s-manifests

    # Setup our storage class first as prometheus will need storage to store the metrics
    kubectl apply -f ./storage/ebs-sc.yaml

    # Now we setup start setting up Prometheus, Grafana and Grafana Loki
    helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
    helm repo add grafana https://grafana.github.io/helm-charts
    helm repo update
    helm install prometheus prometheus-community/kube-prometheus-stack
    helm install --values ./loki-logging/loki-values.yaml loki grafana/loki-stack

    # Setup grafana loki so that it shares the same grafana dashboard with prometheus
    # By adding datasource collected from loki and import it into grafana
    kubectl port-forward deployment/prometheus-grafana 3000

    # Now go to localhost:3000 and log in to grafana dashboard with the following credentials
    # User: admin
    # Password: prom-operator
    # Then go under connections -> add new connection -> search for loki -> on the top right, press "add data source"
    # Then under the connection field, find the URL box and paste http://loki:3100 as the url 
    # then scroll down to press "Save & test" and you're done setting up the monitoring and logging services!

    ```
2. **Step 2**: Deploy the microservices and front-end.
    ```bash
    # Setup config map and secrets
    kubectl apply -f ./frontend/configmap-frontend.yaml
    kubectl apply -f ./microservices/auth/configmap-auth.yaml
    kubectl apply -f ./microservices/main/configmap-main.yaml
    kubectl apply -f ./microservices/user/configmap-user.yaml
    kubectl apply -f ./microservices/verify/configmap-verify.yaml
    kubectl apply -f ./secrets/db-secret.yaml
    kubectl apply -f ./secrets/jwt-secret.yaml
    
    # Setup microserves' statefulset
    kubectl apply -f ./microservices/main/statefulset-main.yaml
    kubectl apply -f ./microservices/user/statefulset-user.yaml
    kubectl apply -f ./microservices/verify/statefulset-verify.yaml

    # Setup microservices' deployment and service
    kubectl apply -f ./microservices/auth/deployment-auth.yaml
    kubectl apply -f ./microservices/main/deployment-main.yaml
    kubectl apply -f ./microservices/user/deployment-user.yaml
    kubectl apply -f ./microservices/verify/deployment-verify.yaml
    kubectl apply -f ./microservices/redis/deployment-redis.yaml

    # Setup front end's deployment and load balancer
    kubectl apply -f ./frontend/deployment-frontend.yaml
    ```

3. **Step 3**: Deploy exporter for microservices.
    ```bash
    helm install main-db-exporter prometheus-community/prometheus-postgres-exporter -f ./prometheus-monitoring/main-db-values.yaml
    helm install user-db-exporter prometheus-community/prometheus-postgres-exporter -f ./prometheus-monitoring/user-db-values.yaml
    helm install verify-db-exporter prometheus-community/prometheus-postgres-exporter -f ./prometheus-monitoring/verify-db-values.yaml
    helm install redis-exporter prometheus-community/prometheus-redis-exporter -f ./prometheus-monitoring/redis-values.yaml
    ```

4. **Step 4**: Deploy operator.
    ```bash
    kubectl apply -f ../psql-operator/src/main/java/com/example/operator/k8smanifest/clusterRole.yaml     
    kubectl apply -f ../psql-operator/src/main/java/com/example/operator/k8smanifest/PostgresUserSync.yaml     
    kubectl apply -f ../psql-operator/src/main/java/com/example/operator/k8smanifest/pgsql-reader.yaml     
    kubectl apply -f ../psql-operator/src/main/java/com/example/operator/k8smanifest/pgsqlreader-deployment.yaml
    ```      

4. **Step 5**: Deploy Zipkin.
    ```bash
    kubectl apply -f ./zipkin/zipkin-deployment.yaml
    ```