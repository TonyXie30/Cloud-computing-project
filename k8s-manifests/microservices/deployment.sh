#!/bin/bash

# deployment
kubectl apply -f ./auth/deployment-auth.yaml
kubectl apply -f ./main/deployment-main.yaml
kubectl apply -f ./user/deployment-user.yaml
kubectl apply -f ./verify/deployment-verify.yaml
kubectl apply -f ./redis/deployment-redis.yaml