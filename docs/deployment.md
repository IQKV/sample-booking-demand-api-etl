# Deployment Guide

## Overview

This project supports multiple deployment strategies for the Booking Demand API ETL microservices architecture. The system consists of four main services that can be deployed using Docker Compose, Kubernetes (Minikube), or individual service deployment.

## Architecture Components

### Core Services

- **edge-service**: API Gateway with GraphQL interface (Port: 8080)
- **dashboard-service**: Backend REST API (Port: 8080)
- **ingestor-service**: Data ingestion service (Port: 8080)
- **init-container-service**: Database migration service

### Infrastructure Components

- **MySQL 5.7.44**: Primary database
- **RabbitMQ**: Message broker with management UI
- **Prometheus**: Metrics collection (Port: 9090)
- **Grafana**: Monitoring dashboard (Port: 3000)

## Deployment Options

### 1. Docker Compose Deployment

#### Development Environment

For local development with full monitoring stack:

```bash
# Start infrastructure services
docker compose up -d mysql rabbitmq prometheus grafana

# Build all services
./mvnw clean package -DskipTests

# Run services individually (in separate terminals)
./mvnw -pl init-container-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw -pl dashboard-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw -pl edge-service spring-boot:run -Dspring-boot.run.profiles=local
./mvnw -pl ingestor-service spring-boot:run -Dspring-boot.run.profiles=local
```

**Access Points:**

- API Gateway: http://localhost:8080
- RabbitMQ Management: http://localhost:15672 (svc_testing/svc_testing)
- Prometheus: http://localhost:9090
- Grafana: http://localhost:3000 (admin/changeme)

#### Demo Environment

For demonstration with pre-built images:

```bash
# Start complete demo environment
docker compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f [service-name]
```

**Access Points:**

- Dashboard Service: http://localhost:8080
- Ingestor Service: http://localhost:8081
- RabbitMQ Management: http://localhost:15672 (demo/demo)

### 2. Kubernetes Deployment

#### Prerequisites

**Required Tools:**

```bash
# Install kubectl
curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
chmod +x kubectl && sudo mv kubectl /usr/local/bin/

# Install minikube (for local development)
curl -Lo minikube https://storage.googleapis.com/minikube/releases/latest/minikube-linux-amd64
chmod +x minikube && sudo mv minikube /usr/local/bin/

# Install Docker (if not already installed)
# Follow Docker installation guide for your OS
```

**Cluster Setup:**

```bash
# Start Minikube with adequate resources
minikube start --memory=4096 --cpus=2 --disk-size=20g

# Enable required addons
minikube addons enable ingress
minikube addons enable metrics-server
minikube addons enable dashboard

# Verify cluster is running
kubectl cluster-info
kubectl get nodes
```

#### Local Kubernetes Deployment (Minikube)

**Quick Start:**

```bash
# Navigate to minikube directory
cd minikube

# Setup cluster and install everything
./scripts/setup-cluster.sh
./scripts/install-all.sh

# Check deployment status
kubectl get pods -A
kubectl get services -A
```

**Step-by-Step Deployment:**

1. **Cluster Initialization:**

```bash
# Set up namespace and RBAC
./scripts/setup-cluster.sh

# This creates:
# - Namespace: default (configurable)
# - Service Account: api-service-account
# - ClusterRole: microservices-kubernetes-namespace-reader
# - ClusterRoleBinding for service discovery
```

2. **Infrastructure Deployment:**

```bash
# Deploy MySQL and RabbitMQ
./scripts/install-infra.sh

# Wait for infrastructure to be ready
kubectl wait --for=condition=ready pod -l app=mysql --timeout=300s
kubectl wait --for=condition=ready pod -l app=rabbitmq --timeout=300s

# Verify infrastructure
kubectl get pods -l tier=infrastructure
kubectl get services -l tier=infrastructure
```

3. **Application Deployment:**

```bash
# Deploy all microservices
./scripts/install-apps.sh

# Wait for applications to be ready
kubectl wait --for=condition=ready pod -l tier=application --timeout=300s

# Check application status
kubectl get deployments
kubectl get pods -l tier=application
```

4. **Ingress Configuration:**

```bash
# Apply ingress rules
kubectl apply -f manifests/ingress.yaml

# Get ingress IP
kubectl get ingress gateway-ingress
```

#### Production Kubernetes Deployment

**Namespace Management:**

```yaml
# production-namespace.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: booking-demand-api-etl-prod
  labels:
    environment: production
    project: booking-demand-api-etl
---
apiVersion: v1
kind: ResourceQuota
metadata:
  name: compute-quota
  namespace: booking-demand-api-etl-prod
spec:
  hard:
    requests.cpu: "4"
    requests.memory: 8Gi
    limits.cpu: "8"
    limits.memory: 16Gi
    persistentvolumeclaims: "10"
```

**ConfigMap and Secrets Management:**

```yaml
# production-config.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: booking-demand-api-etl-prod
data:
  # Database Configuration
  DATASOURCE_URL: "jdbc:mysql://mysql-service:3306/prod_db"

  # RabbitMQ Configuration
  RABBITMQ_HOST: "rabbitmq-service"
  RABBITMQ_PORT: "5672"

  # Logging Configuration
  LOGGING_LEVEL_ROOT: "INFO"
  LOGGING_LEVEL_APP: "INFO"

  # Spring Configuration
  SPRING_PROFILES_ACTIVE: "prod"

  # JVM Configuration
  JAVA_OPTS: "-Xms512m -Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

---
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
  namespace: booking-demand-api-etl-prod
type: Opaque
stringData:
  # Database Credentials
  DATASOURCE_USERNAME: "prod_user"
  DATASOURCE_PASSWORD: "secure_prod_password"

  # RabbitMQ Credentials
  RABBITMQ_USERNAME: "prod_rabbitmq_user"
  RABBITMQ_PASSWORD: "secure_rabbitmq_password"

  # Additional secrets
  JWT_SECRET: "your-jwt-secret-key"
  ENCRYPTION_KEY: "your-encryption-key"
```

**Production Deployment Manifests:**

_Dashboard Service Production Deployment:_

```yaml
# dashboard-service-prod.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: dashboard-service
  namespace: booking-demand-api-etl-prod
  labels:
    app: dashboard-service
    tier: application
    version: v1
spec:
  replicas: 3
  strategy:
    type: RollingUpdate
    rollingUpdate:
      maxSurge: 1
      maxUnavailable: 0
  selector:
    matchLabels:
      app: dashboard-service
  template:
    metadata:
      labels:
        app: dashboard-service
        tier: application
        version: v1
      annotations:
        prometheus.io/scrape: "true"
        prometheus.io/port: "8080"
        prometheus.io/path: "/actuator/prometheus"
    spec:
      serviceAccountName: api-service-account
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
        fsGroup: 1000
      containers:
        - name: dashboard-service
          image: your-registry/dashboard-service:v1.0.0
          imagePullPolicy: Always
          ports:
            - containerPort: 8080
              name: http
              protocol: TCP
          env:
            - name: SPRING_PROFILES_ACTIVE
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: SPRING_PROFILES_ACTIVE
            - name: DATASOURCE_URL
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: DATASOURCE_URL
            - name: DATASOURCE_USERNAME
              valueFrom:
                secretKeyRef:
                  name: app-secrets
                  key: DATASOURCE_USERNAME
            - name: DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: app-secrets
                  key: DATASOURCE_PASSWORD
            - name: RABBITMQ_HOST
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: RABBITMQ_HOST
            - name: RABBITMQ_USERNAME
              valueFrom:
                secretKeyRef:
                  name: app-secrets
                  key: RABBITMQ_USERNAME
            - name: RABBITMQ_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: app-secrets
                  key: RABBITMQ_PASSWORD
            - name: JAVA_OPTS
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: JAVA_OPTS
          resources:
            requests:
              cpu: "500m"
              memory: "1Gi"
            limits:
              cpu: "2"
              memory: "2Gi"
          readinessProbe:
            httpGet:
              port: 8080
              path: /actuator/health/readiness
            initialDelaySeconds: 60
            timeoutSeconds: 10
            periodSeconds: 30
            failureThreshold: 3
          livenessProbe:
            httpGet:
              port: 8080
              path: /actuator/health/liveness
            initialDelaySeconds: 90
            timeoutSeconds: 10
            periodSeconds: 30
            failureThreshold: 3
          startupProbe:
            httpGet:
              port: 8080
              path: /actuator/health/readiness
            initialDelaySeconds: 30
            timeoutSeconds: 10
            periodSeconds: 10
            failureThreshold: 30
          volumeMounts:
            - name: tmp
              mountPath: /tmp
            - name: logs
              mountPath: /app/logs
      volumes:
        - name: tmp
          emptyDir: {}
        - name: logs
          emptyDir: {}
      affinity:
        podAntiAffinity:
          preferredDuringSchedulingIgnoredDuringExecution:
            - weight: 100
              podAffinityTerm:
                labelSelector:
                  matchExpressions:
                    - key: app
                      operator: In
                      values:
                        - dashboard-service
                topologyKey: kubernetes.io/hostname
---
apiVersion: v1
kind: Service
metadata:
  name: dashboard-service
  namespace: booking-demand-api-etl-prod
  labels:
    app: dashboard-service
spec:
  type: ClusterIP
  ports:
    - port: 8080
      targetPort: 8080
      protocol: TCP
      name: http
  selector:
    app: dashboard-service
---
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: dashboard-service-pdb
  namespace: booking-demand-api-etl-prod
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: dashboard-service
```

**Persistent Storage Configuration:**

```yaml
# mysql-storage.yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
  namespace: booking-demand-api-etl-prod
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 20Gi
  storageClassName: fast-ssd
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: rabbitmq-pvc
  namespace: booking-demand-api-etl-prod
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 10Gi
  storageClassName: fast-ssd
```

#### Helm Chart Deployment

**Create Helm Chart Structure:**

```bash
# Create Helm chart
helm create booking-demand-api-etl

# Directory structure
booking-demand-api-etl/
├── Chart.yaml
├── values.yaml
├── values-prd.yaml
├── values-uat.yaml
└── templates/
    ├── configmap.yaml
    ├── secret.yaml
    ├── dashboard-service/
    ├── edge-service/
    ├── ingestor-service/
    └── infrastructure/
```

**Helm Values for Production:**

```yaml
# values-prd.yaml
global:
  environment: production
  namespace: booking-demand-api-etl-prod
  imageRegistry: your-registry.com
  imageTag: v1.0.0

replicaCount:
  dashboardService: 3
  edgeService: 2
  ingestorService: 2

resources:
  dashboardService:
    requests:
      cpu: 500m
      memory: 1Gi
    limits:
      cpu: 2
      memory: 2Gi
  edgeService:
    requests:
      cpu: 300m
      memory: 512Mi
    limits:
      cpu: 1
      memory: 1Gi

database:
  host: mysql-service
  name: prod_db
  storage: 50Gi

rabbitmq:
  host: rabbitmq-service
  storage: 20Gi

ingress:
  enabled: true
  className: nginx
  host: api.yourdomain.com
  tls:
    enabled: true
    secretName: api-tls-secret

monitoring:
  prometheus:
    enabled: true
  grafana:
    enabled: true
```

**Deploy with Helm:**

```bash
# Install/upgrade production deployment
helm upgrade --install booking-demand-api-etl ./booking-demand-api-etl \
  -f values-prd.yaml \
  --namespace booking-demand-api-etl-prod \
  --create-namespace

# Check deployment status
helm status booking-demand-api-etl -n booking-demand-api-etl-prod
helm list -n booking-demand-api-etl-prod
```

#### Service Access and Networking

**Ingress Configuration:**

```yaml
# production-ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: api-ingress
  namespace: booking-demand-api-etl-prod
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/use-regex: "true"
    nginx.ingress.kubernetes.io/rewrite-target: /$2
    cert-manager.io/cluster-issuer: letsencrypt-prod
    nginx.ingress.kubernetes.io/rate-limit: "100"
    nginx.ingress.kubernetes.io/rate-limit-window: "1m"
spec:
  tls:
    - hosts:
        - api.yourdomain.com
      secretName: api-tls-secret
  rules:
    - host: api.yourdomain.com
      http:
        paths:
          - path: /api/dashboard(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: dashboard-service
                port:
                  number: 8080
          - path: /api/edge(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: edge-service
                port:
                  number: 8080
          - path: /api/ingestor(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: ingestor-service
                port:
                  number: 8080
```

**Network Policies:**

```yaml
# network-policy.yaml
apiVersion: networking.k8s.io/v1
kind: NetworkPolicy
metadata:
  name: api-network-policy
  namespace: booking-demand-api-etl-prod
spec:
  podSelector:
    matchLabels:
      tier: application
  policyTypes:
    - Ingress
    - Egress
  ingress:
    - from:
        - namespaceSelector:
            matchLabels:
              name: ingress-nginx
      ports:
        - protocol: TCP
          port: 8080
    - from:
        - podSelector:
            matchLabels:
              tier: application
      ports:
        - protocol: TCP
          port: 8080
  egress:
    - to:
        - podSelector:
            matchLabels:
              app: mysql
      ports:
        - protocol: TCP
          port: 3306
    - to:
        - podSelector:
            matchLabels:
              app: rabbitmq
      ports:
        - protocol: TCP
          port: 5672
    - to: []
      ports:
        - protocol: TCP
          port: 53
        - protocol: UDP
          port: 53
```

#### Access Services

**Local Access (Minikube):**

```bash
# Get cluster IP
minikube ip

# Port forwarding for direct access
kubectl port-forward service/dashboard-service 8080:8080
kubectl port-forward service/edge-service 8081:8080
kubectl port-forward service/rabbitmq 15672:15672

# Access via ingress (add to /etc/hosts)
echo "$(minikube ip) bcdb.info" | sudo tee -a /etc/hosts

# Test endpoints
curl http://bcdb.info/dashboard/actuator/health
curl http://bcdb.info/edge/actuator/health
```

**Production Access:**

```bash
# Via ingress
curl https://api.yourdomain.com/api/dashboard/actuator/health
curl https://api.yourdomain.com/api/edge/actuator/health

# Internal service discovery
kubectl exec -it dashboard-service-pod -- curl http://edge-service:8080/actuator/health
```

#### Monitoring and Observability

**Prometheus ServiceMonitor:**

```yaml
# service-monitor.yaml
apiVersion: monitoring.coreos.com/v1
kind: ServiceMonitor
metadata:
  name: booking-demand-api-etl
  namespace: booking-demand-api-etl-prod
  labels:
    app: booking-demand-api-etl
spec:
  selector:
    matchLabels:
      tier: application
  endpoints:
    - port: http
      path: /actuator/prometheus
      interval: 30s
      scrapeTimeout: 10s
```

**Grafana Dashboard ConfigMap:**

```yaml
# grafana-dashboard.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: booking-demand-api-etl-dashboard
  namespace: monitoring
  labels:
    grafana_dashboard: "1"
data:
  booking-demand-api-etl.json: |
    {
      "dashboard": {
        "title": "Booking Demand API ETL",
        "panels": [
          {
            "title": "Service Health",
            "type": "stat",
            "targets": [
              {
                "expr": "up{job=\"booking-demand-api-etl\"}"
              }
            ]
          }
        ]
      }
    }
```

### 3. Production Deployment

#### Environment Configuration

Create environment-specific configuration files:

**application-production.yml**:

```yaml
spring:
  datasource:
    url: ${DATASOURCE_URL}
    username: ${DATASOURCE_USERNAME}
    password: ${DATASOURCE_PASSWORD}
  rabbitmq:
    host: ${RABBITMQ_HOST}
    port: ${RABBITMQ_PORT}
    username: ${RABBITMQ_USERNAME}
    password: ${RABBITMQ_PASSWORD}

logging:
  level:
    com.iqkv.sample.bookingdemandapietl: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
```

#### Docker Image Building

```bash
# Build all services
./mvnw clean package -DskipTests

# Build Docker images
docker build -t your-registry/dashboard-service:latest dashboard-service/
docker build -t your-registry/edge-service:latest edge-service/
docker build -t your-registry/ingestor-service:latest ingestor-service/
docker build -t your-registry/init-container-service:latest init-container-service/

# Push to registry
docker push your-registry/dashboard-service:latest
docker push your-registry/edge-service:latest
docker push your-registry/ingestor-service:latest
docker push your-registry/init-container-service:latest
```

#### Kubernetes Production Deployment

```yaml
# values-prd.yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
data:
  DATASOURCE_URL: "jdbc:mysql://mysql-service:3306/prod_db"
  RABBITMQ_HOST: "rabbitmq-service"
  RABBITMQ_PORT: "5672"
---
apiVersion: v1
kind: Secret
metadata:
  name: app-secrets
type: Opaque
stringData:
  DATASOURCE_USERNAME: "prod_user"
  DATASOURCE_PASSWORD: "secure_password"
  RABBITMQ_USERNAME: "prod_user"
  RABBITMQ_PASSWORD: "secure_password"
```

## Configuration Management

### Environment Variables

| Variable                 | Description          | Default   | Required |
| ------------------------ | -------------------- | --------- | -------- |
| `DATASOURCE_URL`         | MySQL connection URL | -         | Yes      |
| `DATASOURCE_USERNAME`    | Database username    | -         | Yes      |
| `DATASOURCE_PASSWORD`    | Database password    | -         | Yes      |
| `RABBITMQ_HOST`          | RabbitMQ hostname    | localhost | Yes      |
| `RABBITMQ_PORT`          | RabbitMQ port        | 5672      | No       |
| `RABBITMQ_USERNAME`      | RabbitMQ username    | -         | Yes      |
| `RABBITMQ_PASSWORD`      | RabbitMQ password    | -         | Yes      |
| `SERVER_PORT`            | Service port         | 8080      | No       |
| `SPRING_PROFILES_ACTIVE` | Active profile       | default   | No       |

### Spring Profiles

- **local**: Development with enhanced logging and debugging
- **dev**: Development environment with external services
- **prod**: Production with optimized settings and security

## Health Checks and Monitoring

### Health Endpoints

All services expose Spring Boot Actuator endpoints:

```bash
# Health check
curl http://localhost:8080/actuator/health

# Detailed health with components
curl http://localhost:8080/actuator/health/readiness
curl http://localhost:8080/actuator/health/liveness

# Metrics
curl http://localhost:8080/actuator/metrics
curl http://localhost:8080/actuator/prometheus
```

### Monitoring Setup

#### Prometheus Configuration

Services are configured to expose metrics at `/actuator/prometheus`. Prometheus scrapes these endpoints automatically when using the provided configuration.

#### Grafana Dashboards

Pre-configured dashboards are available in `docker/grafana/provisioning/dashboards/` for:

- JVM metrics
- HTTP request metrics
- Database connection pools
- RabbitMQ metrics

## Scaling and Load Balancing

### Horizontal Scaling

```bash
# Scale services in Kubernetes
kubectl scale deployment dashboard-service --replicas=3
kubectl scale deployment edge-service --replicas=2
kubectl scale deployment ingestor-service --replicas=2

# Scale with Docker Compose
docker compose up -d --scale dashboard-service=3
```

### Load Balancing

- **Kubernetes**: Built-in service load balancing
- **Docker Compose**: Use external load balancer (nginx, HAProxy)
- **Production**: Application Load Balancer (ALB) or similar

## Security Considerations

### Network Security

- Use private networks for service communication
- Expose only necessary ports externally
- Implement proper firewall rules

### Secrets Management

- Use Kubernetes secrets or external secret management
- Never commit credentials to version control
- Rotate credentials regularly

### Database Security

- Use dedicated database users with minimal privileges
- Enable SSL/TLS for database connections
- Regular security updates

## Backup and Recovery

### Database Backup

```bash
# MySQL backup
kubectl exec mysql-pod -- mysqldump -u root -p$MYSQL_ROOT_PASSWORD prod_db > backup.sql

# Restore
kubectl exec -i mysql-pod -- mysql -u root -p$MYSQL_ROOT_PASSWORD prod_db < backup.sql
```

### Configuration Backup

- Version control all configuration files
- Backup Kubernetes manifests and secrets
- Document environment-specific configurations

## Kubernetes Operations

### Cluster Management

**Cluster Information:**

```bash
# Cluster status
kubectl cluster-info
kubectl get nodes -o wide
kubectl top nodes

# Resource usage
kubectl describe node <node-name>
kubectl get events --sort-by=.metadata.creationTimestamp
```

**Namespace Operations:**

```bash
# List all namespaces
kubectl get namespaces

# Create namespace
kubectl create namespace booking-demand-api-etl-staging

# Delete namespace (careful!)
kubectl delete namespace booking-demand-api-etl-staging
```

### Application Management

**Deployment Operations:**

```bash
# Check deployment status
kubectl get deployments -n booking-demand-api-etl-prod
kubectl rollout status deployment/dashboard-service -n booking-demand-api-etl-prod

# Scale deployments
kubectl scale deployment dashboard-service --replicas=5 -n booking-demand-api-etl-prod

# Update deployment image
kubectl set image deployment/dashboard-service \
  dashboard-service=your-registry/dashboard-service:v1.1.0 \
  -n booking-demand-api-etl-prod

# Restart deployment
kubectl rollout restart deployment/dashboard-service -n booking-demand-api-etl-prod
```

**Pod Management:**

```bash
# List pods with details
kubectl get pods -o wide -n booking-demand-api-etl-prod
kubectl get pods --show-labels -n booking-demand-api-etl-prod

# Pod logs
kubectl logs -f deployment/dashboard-service -n booking-demand-api-etl-prod
kubectl logs --previous pod-name -n booking-demand-api-etl-prod

# Execute commands in pods
kubectl exec -it dashboard-service-pod -n booking-demand-api-etl-prod -- /bin/bash
kubectl exec -it dashboard-service-pod -n booking-demand-api-etl-prod -- curl localhost:8080/actuator/health
```

**Service and Networking:**

```bash
# List services
kubectl get services -n booking-demand-api-etl-prod
kubectl get endpoints -n booking-demand-api-etl-prod

# Test service connectivity
kubectl run test-pod --image=busybox --rm -it --restart=Never -- \
  wget -qO- http://dashboard-service:8080/actuator/health

# Port forwarding
kubectl port-forward service/dashboard-service 8080:8080 -n booking-demand-api-etl-prod
```

### Configuration Management

**ConfigMaps and Secrets:**

```bash
# List configurations
kubectl get configmaps -n booking-demand-api-etl-prod
kubectl get secrets -n booking-demand-api-etl-prod

# View configuration
kubectl describe configmap app-config -n booking-demand-api-etl-prod
kubectl get secret app-secrets -o yaml -n booking-demand-api-etl-prod

# Update configuration
kubectl create configmap app-config --from-file=application.yml --dry-run=client -o yaml | \
  kubectl apply -n booking-demand-api-etl-prod -f -

# Restart pods after config change
kubectl rollout restart deployment/dashboard-service -n booking-demand-api-etl-prod
```

### Storage Management

**Persistent Volumes:**

```bash
# List storage
kubectl get pv
kubectl get pvc -n booking-demand-api-etl-prod

# Check storage usage
kubectl describe pvc mysql-pvc -n booking-demand-api-etl-prod

# Backup persistent data
kubectl exec mysql-pod -n booking-demand-api-etl-prod -- \
  mysqldump -u root -p$MYSQL_ROOT_PASSWORD prod_db > backup-$(date +%Y%m%d).sql
```

## Troubleshooting

### Kubernetes-Specific Issues

#### Pod Startup Issues

```bash
# Check pod status and events
kubectl describe pod dashboard-service-pod -n booking-demand-api-etl-prod
kubectl get events --field-selector involvedObject.name=dashboard-service-pod -n booking-demand-api-etl-prod

# Check resource constraints
kubectl top pods -n booking-demand-api-etl-prod
kubectl describe node <node-name>

# Check image pull issues
kubectl get pods -o jsonpath='{.items[*].status.containerStatuses[*].state}' -n booking-demand-api-etl-prod
```

#### Service Discovery Issues

```bash
# Test DNS resolution
kubectl run test-dns --image=busybox --rm -it --restart=Never -- \
  nslookup dashboard-service.booking-demand-api-etl-prod.svc.cluster.local

# Check service endpoints
kubectl get endpoints dashboard-service -n booking-demand-api-etl-prod

# Test service connectivity
kubectl exec -it test-pod -n booking-demand-api-etl-prod -- \
  telnet dashboard-service 8080
```

#### Ingress Issues

```bash
# Check ingress status
kubectl get ingress -n booking-demand-api-etl-prod
kubectl describe ingress api-ingress -n booking-demand-api-etl-prod

# Check ingress controller logs
kubectl logs -n ingress-nginx deployment/ingress-nginx-controller

# Test ingress routing
curl -H "Host: api.yourdomain.com" http://<ingress-ip>/api/dashboard/actuator/health
```

#### Storage Issues

```bash
# Check PVC status
kubectl get pvc -n booking-demand-api-etl-prod
kubectl describe pvc mysql-pvc -n booking-demand-api-etl-prod

# Check storage class
kubectl get storageclass
kubectl describe storageclass fast-ssd

# Check volume mounts
kubectl describe pod mysql-pod -n booking-demand-api-etl-prod | grep -A 10 "Mounts:"
```

### Common Issues

#### Service Startup Failures

```bash
# Check logs
kubectl logs deployment/dashboard-service -n booking-demand-api-etl-prod
docker-compose logs dashboard-service

# Check resource usage
kubectl top pods -n booking-demand-api-etl-prod
docker stats

# Check health endpoints
kubectl exec -it dashboard-service-pod -n booking-demand-api-etl-prod -- \
  curl localhost:8080/actuator/health
```

#### Database Connection Issues

```bash
# Test database connectivity
kubectl exec -it mysql-pod -n booking-demand-api-etl-prod -- \
  mysql -u $DB_USER -p$DB_PASSWORD -e "SELECT 1"

# Check network connectivity
kubectl exec -it dashboard-service-pod -n booking-demand-api-etl-prod -- \
  nc -zv mysql-service 3306

# Check database service
kubectl get service mysql -n booking-demand-api-etl-prod
kubectl get endpoints mysql -n booking-demand-api-etl-prod
```

#### RabbitMQ Issues

```bash
# Check RabbitMQ status
kubectl exec -it rabbitmq-pod -n booking-demand-api-etl-prod -- \
  rabbitmqctl status

# Check queues
kubectl exec -it rabbitmq-pod -n booking-demand-api-etl-prod -- \
  rabbitmqctl list_queues

# Check RabbitMQ service
kubectl get service rabbitmq -n booking-demand-api-etl-prod
kubectl port-forward service/rabbitmq 15672:15672 -n booking-demand-api-etl-prod
```

#### Memory and CPU Issues

```bash
# Check resource usage
kubectl top pods -n booking-demand-api-etl-prod
kubectl top nodes

# Check resource limits
kubectl describe pod dashboard-service-pod -n booking-demand-api-etl-prod | grep -A 10 "Limits:"

# Check OOMKilled pods
kubectl get pods -n booking-demand-api-etl-prod | grep OOMKilled
kubectl describe pod <oomkilled-pod> -n booking-demand-api-etl-prod
```

### Performance Tuning

#### JVM Tuning

```bash
# Set JVM options
export JAVA_OPTS="-Xms512m -Xmx2g -XX:+UseG1GC"
```

#### Database Optimization

- Monitor slow queries
- Optimize indexes
- Configure connection pooling

## Rollback Procedures

### Kubernetes Rollback

```bash
# Check rollout history
kubectl rollout history deployment/dashboard-service

# Rollback to previous version
kubectl rollout undo deployment/dashboard-service

# Rollback to specific revision
kubectl rollout undo deployment/dashboard-service --to-revision=2
```

### Docker Compose Rollback

```bash
# Stop current version
docker compose down

# Deploy previous version
docker-compose -f docker-compose.previous.yml up -d
```

## Maintenance

### Regular Maintenance Tasks

- Update base images and dependencies
- Monitor disk space and logs
- Review and rotate secrets
- Update SSL certificates
- Performance monitoring and optimization

### Scheduled Maintenance

- Database maintenance windows
- Service updates and patches
- Infrastructure updates
- Backup verification

## Advanced Kubernetes Features

### Horizontal Pod Autoscaling (HPA)

**CPU-based Autoscaling:**

```yaml
# hpa-dashboard-service.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: dashboard-service-hpa
  namespace: booking-demand-api-etl-prod
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: dashboard-service
  minReplicas: 2
  maxReplicas: 10
  metrics:
    - type: Resource
      resource:
        name: cpu
        target:
          type: Utilization
          averageUtilization: 70
    - type: Resource
      resource:
        name: memory
        target:
          type: Utilization
          averageUtilization: 80
  behavior:
    scaleDown:
      stabilizationWindowSeconds: 300
      policies:
        - type: Percent
          value: 10
          periodSeconds: 60
    scaleUp:
      stabilizationWindowSeconds: 60
      policies:
        - type: Percent
          value: 50
          periodSeconds: 60
```

**Custom Metrics Autoscaling:**

```yaml
# hpa-custom-metrics.yaml
apiVersion: autoscaling/v2
kind: HorizontalPodAutoscaler
metadata:
  name: ingestor-service-hpa
  namespace: booking-demand-api-etl-prod
spec:
  scaleTargetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: ingestor-service
  minReplicas: 1
  maxReplicas: 5
  metrics:
    - type: Pods
      pods:
        metric:
          name: rabbitmq_queue_messages
        target:
          type: AverageValue
          averageValue: "10"
```

### Vertical Pod Autoscaling (VPA)

```yaml
# vpa-dashboard-service.yaml
apiVersion: autoscaling.k8s.io/v1
kind: VerticalPodAutoscaler
metadata:
  name: dashboard-service-vpa
  namespace: booking-demand-api-etl-prod
spec:
  targetRef:
    apiVersion: apps/v1
    kind: Deployment
    name: dashboard-service
  updatePolicy:
    updateMode: "Auto"
  resourcePolicy:
    containerPolicies:
      - containerName: dashboard-service
        minAllowed:
          cpu: 100m
          memory: 128Mi
        maxAllowed:
          cpu: 2
          memory: 4Gi
        controlledResources: ["cpu", "memory"]
```

### Pod Disruption Budgets

```yaml
# pdb-services.yaml
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: dashboard-service-pdb
  namespace: booking-demand-api-etl-prod
spec:
  minAvailable: 2
  selector:
    matchLabels:
      app: dashboard-service
---
apiVersion: policy/v1
kind: PodDisruptionBudget
metadata:
  name: edge-service-pdb
  namespace: booking-demand-api-etl-prod
spec:
  maxUnavailable: 1
  selector:
    matchLabels:
      app: edge-service
```

### Service Mesh Integration (Istio)

**Service Mesh Configuration:**

```yaml
# istio-gateway.yaml
apiVersion: networking.istio.io/v1beta1
kind: Gateway
metadata:
  name: booking-demand-api-gateway
  namespace: booking-demand-api-etl-prod
spec:
  selector:
    istio: ingressgateway
  servers:
    - port:
        number: 443
        name: https
        protocol: HTTPS
      tls:
        mode: SIMPLE
        credentialName: api-tls-secret
      hosts:
        - api.yourdomain.com
---
apiVersion: networking.istio.io/v1beta1
kind: VirtualService
metadata:
  name: booking-demand-api-vs
  namespace: booking-demand-api-etl-prod
spec:
  hosts:
    - api.yourdomain.com
  gateways:
    - booking-demand-api-gateway
  http:
    - match:
        - uri:
            prefix: /api/dashboard
      route:
        - destination:
            host: dashboard-service
            port:
              number: 8080
      fault:
        delay:
          percentage:
            value: 0.1
          fixedDelay: 5s
    - match:
        - uri:
            prefix: /api/edge
      route:
        - destination:
            host: edge-service
            port:
              number: 8080
```

**Circuit Breaker Configuration:**

```yaml
# destination-rule.yaml
apiVersion: networking.istio.io/v1beta1
kind: DestinationRule
metadata:
  name: dashboard-service-dr
  namespace: booking-demand-api-etl-prod
spec:
  host: dashboard-service
  trafficPolicy:
    connectionPool:
      tcp:
        maxConnections: 100
      http:
        http1MaxPendingRequests: 50
        maxRequestsPerConnection: 10
    outlierDetection:
      consecutiveErrors: 3
      interval: 30s
      baseEjectionTime: 30s
      maxEjectionPercent: 50
```

### GitOps Deployment with ArgoCD

**Application Configuration:**

```yaml
# argocd-application.yaml
apiVersion: argoproj.io/v1alpha1
kind: Application
metadata:
  name: booking-demand-api-etl
  namespace: argocd
spec:
  project: default
  source:
    repoURL: https://github.com/your-org/booking-demand-api-etl-k8s
    targetRevision: HEAD
    path: manifests/production
  destination:
    server: https://kubernetes.default.svc
    namespace: booking-demand-api-etl-prod
  syncPolicy:
    automated:
      prune: true
      selfHeal: true
    syncOptions:
      - CreateNamespace=true
    retry:
      limit: 5
      backoff:
        duration: 5s
        factor: 2
        maxDuration: 3m
```

### Backup and Disaster Recovery

**Velero Backup Configuration:**

```yaml
# velero-backup.yaml
apiVersion: velero.io/v1
kind: Backup
metadata:
  name: booking-demand-api-etl-backup
  namespace: velero
spec:
  includedNamespaces:
    - booking-demand-api-etl-prod
  includedResources:
    - persistentvolumeclaims
    - persistentvolumes
    - secrets
    - configmaps
  storageLocation: default
  volumeSnapshotLocations:
    - default
  ttl: 720h0m0s
---
apiVersion: velero.io/v1
kind: Schedule
metadata:
  name: booking-demand-api-etl-daily-backup
  namespace: velero
spec:
  schedule: "0 2 * * *"
  template:
    includedNamespaces:
      - booking-demand-api-etl-prod
    storageLocation: default
    ttl: 720h0m0s
```

**Database Backup CronJob:**

```yaml
# mysql-backup-cronjob.yaml
apiVersion: batch/v1
kind: CronJob
metadata:
  name: mysql-backup
  namespace: booking-demand-api-etl-prod
spec:
  schedule: "0 3 * * *"
  jobTemplate:
    spec:
      template:
        spec:
          containers:
            - name: mysql-backup
              image: mysql:5.7.44
              command:
                - /bin/bash
                - -c
                - |
                  mysqldump -h mysql -u $MYSQL_USER -p$MYSQL_PASSWORD prod_db > /backup/backup-$(date +%Y%m%d-%H%M%S).sql
                  find /backup -name "backup-*.sql" -mtime +7 -delete
              env:
                - name: MYSQL_USER
                  valueFrom:
                    secretKeyRef:
                      name: app-secrets
                      key: DATASOURCE_USERNAME
                - name: MYSQL_PASSWORD
                  valueFrom:
                    secretKeyRef:
                      name: app-secrets
                      key: DATASOURCE_PASSWORD
              volumeMounts:
                - name: backup-storage
                  mountPath: /backup
          volumes:
            - name: backup-storage
              persistentVolumeClaim:
                claimName: mysql-backup-pvc
          restartPolicy: OnFailure
```

### Security Best Practices

**Pod Security Standards:**

```yaml
# pod-security-policy.yaml
apiVersion: v1
kind: Namespace
metadata:
  name: booking-demand-api-etl-prod
  labels:
    pod-security.kubernetes.io/enforce: restricted
    pod-security.kubernetes.io/audit: restricted
    pod-security.kubernetes.io/warn: restricted
```

**Security Context:**

```yaml
# security-context.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: dashboard-service
spec:
  template:
    spec:
      securityContext:
        runAsNonRoot: true
        runAsUser: 1000
        runAsGroup: 1000
        fsGroup: 1000
        seccompProfile:
          type: RuntimeDefault
      containers:
        - name: dashboard-service
          securityContext:
            allowPrivilegeEscalation: false
            readOnlyRootFilesystem: true
            capabilities:
              drop:
                - ALL
          volumeMounts:
            - name: tmp
              mountPath: /tmp
            - name: cache
              mountPath: /app/cache
      volumes:
        - name: tmp
          emptyDir: {}
        - name: cache
          emptyDir: {}
```

### Multi-Environment Management

**Environment-Specific Overlays (Kustomize):**

```yaml
# kustomization.yaml (base)
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

resources:
  - dashboard-service.yaml
  - edge-service.yaml
  - ingestor-service.yaml
  - mysql.yaml
  - rabbitmq.yaml

commonLabels:
  app.kubernetes.io/name: booking-demand-api-etl
  app.kubernetes.io/version: v1.0.0
```

```yaml
# overlays/production/kustomization.yaml
apiVersion: kustomize.config.k8s.io/v1beta1
kind: Kustomization

namespace: booking-demand-api-etl-prod

resources:
  - ../../base

patchesStrategicMerge:
  - dashboard-service-prod.yaml
  - mysql-prod.yaml

replicas:
  - name: dashboard-service
    count: 3
  - name: edge-service
    count: 2

images:
  - name: dashboard-service
    newTag: v1.0.0
  - name: edge-service
    newTag: v1.0.0
```

**Deploy with Kustomize:**

```bash
# Deploy to production
kubectl apply -k overlays/production

# Deploy to staging
kubectl apply -k overlays/staging

# Preview changes
kubectl diff -k overlays/production
```
