#!/bin/bash

# set -e
set -x

. ./set-env.sh

cd ../k8s || exit

kubectl config use-context $CLUSTER_NAME

kubectl apply -n $K8S_NAMESPACE -f mysql/configmap.yaml
kubectl apply -n $K8S_NAMESPACE -f mysql/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f mysql/deployment.yaml

kubectl apply -n $K8S_NAMESPACE -f rabbitmq/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f rabbitmq/deployment.yaml



cd ../scripts || exit
