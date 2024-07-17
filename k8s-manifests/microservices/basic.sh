#!/bin/bash

# config map
kubectl apply -f ./auth/configmap-auth.yaml
kubectl apply -f ./main/configmap-main.yaml
kubectl apply -f ./user/configmap-user.yaml
kubectl apply -f ./verify/configmap-verify.yaml

# Storage
kubectl apply -f ./main/statefulset-main.yaml
kubectl apply -f ./user/statefulset-user.yaml
kubectl apply -f ./verify/statefulset-verify.yaml
