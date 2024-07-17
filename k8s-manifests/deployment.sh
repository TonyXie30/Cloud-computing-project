#!/bin/bash

# deployment
kubectl apply -f ./microservices/auth/deployment-auth.yaml
kubectl apply -f ./microservices/main/deployment-main.yaml
kubectl apply -f ./microservices/user/deployment-user.yaml
kubectl apply -f ./microservices/verify/deployment-verify.yaml
kubectl apply -f ./microservices/redis/deployment-redis.yaml