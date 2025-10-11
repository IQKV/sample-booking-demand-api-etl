#!/bin/bash

set -x

.  ./set-env.sh

kubectl config set-context $CLUSTER_NAME
kubectl config use-context $CLUSTER_NAME

kubectl delete -n $K8S_NAMESPACE deployment ingestor-service
kubectl delete -n $K8S_NAMESPACE service ingestor-service
kubectl delete -n $K8S_NAMESPACE configmap ingestor-service
kubectl delete -n $K8S_NAMESPACE secret ingestor-service

kubectl delete -n $K8S_NAMESPACE deployment edge-service
kubectl delete -n $K8S_NAMESPACE service edge-service
kubectl delete -n $K8S_NAMESPACE configmap edge-service
kubectl delete -n $K8S_NAMESPACE secret edge-service

kubectl delete -n $K8S_NAMESPACE deployment dashboard-service
kubectl delete -n $K8S_NAMESPACE service dashboard-service
kubectl delete -n $K8S_NAMESPACE configmap dashboard-service
kubectl delete -n $K8S_NAMESPACE secret dashboard-service

kubectl delete -n $K8S_NAMESPACE deployment init-container-service
kubectl delete -n $K8S_NAMESPACE service init-container-service
kubectl delete -n $K8S_NAMESPACE configmap init-container-service
kubectl delete -n $K8S_NAMESPACE secret init-container-service
