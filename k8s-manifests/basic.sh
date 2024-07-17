#!/bin/bash

# config map
kubectl apply -f ./microservices/auth/configmap-auth.yaml
kubectl apply -f ./microservices/main/configmap-main.yaml
kubectl apply -f ./microservices/user/configmap-user.yaml
kubectl apply -f ./microservices/verify/configmap-verify.yaml
# secrets
kubectl apply -f ./secrets/db-secret.yaml
kubectl apply -f ./secrets/jwt-secret.yaml
# Storage
kubectl apply -f ./storage/ebs-sc.yaml
kubectl apply -f ./microservices/main/statefulset-main.yaml
kubectl apply -f ./microservices/user/statefulset-user.yaml
kubectl apply -f ./microservices/verify/statefulset-verify.yaml
