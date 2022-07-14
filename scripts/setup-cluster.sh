#!/bin/bash

set -x

. ./set-env.sh

kubectl config set-context $CLUSTER_NAME
kubectl config use-context $CLUSTER_NAME

kubectl create namespace $K8S_NAMESPACE

# create clusterRole
kubectl apply  -f ../k8s/rbac-cluster-role.yaml

# creat service account in each namespace
kubectl create serviceaccount $SA_NAME -n $K8S_NAMESPACE

# bind service accounts to clusterRole
kubectl create clusterrolebinding service-pod-reader-$K8S_NAMESPACE --clusterrole=microservices-kubernetes-namespace-reader --serviceaccount=$K8S_NAMESPACE:$SA_NAME



