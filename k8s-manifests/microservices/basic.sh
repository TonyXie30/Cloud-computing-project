#!/bin/bash

# config map
kubectl apply -f ./auth/configmap-auth.yaml
kubectl apply -f ./main/configmap-main.yaml
kubectl apply -f ./user/configmap-user.yaml
kubectl apply -f ./verify/configmap-verify.yaml
# secrets
kubectl apply -f ./secret/secret-auth.yaml
kubectl apply -f ./secret/secret-main.yaml
kubectl apply -f ./secret/secret-user.yaml
kubectl apply -f ./secret/secret-verify.yaml
# Storage
kubectl apply -f ./Storage/ebs.sc.yaml
kubectl apply -f ./main/statefulset-main.yaml
kubectl apply -f ./user/statefulset-user.yaml
kubectl apply -f ./verify/statefulset-verify.yaml
