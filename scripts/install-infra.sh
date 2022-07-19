#!/bin/bash

# set -e
set -x

. ./set-env.sh

cd ../k8s || exit

kubectl config use-context $CLUSTER_NAME

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm install -n $K8S_NAMESPACE prometheus prometheus-community/prometheus
kubectl expose service prometheus-server -n $K8S_NAMESPACE --type=NodePort --target-port=9090 --name=prometheus-server-np

helm repo add grafana https://grafana.github.io/helm-charts
helm install -n $K8S_NAMESPACE grafana stable/grafana
kubectl expose service grafana -n $K8S_NAMESPACE --type=NodePort --target-port=3000 --name=grafana-np
kubectl get secret -n $K8S_NAMESPACE grafana -o jsonpath="{.data.admin-password}" | base64 --decode ; echo

kubectl apply -n $K8S_NAMESPACE -f mysql/configmap.yaml
kubectl apply -n $K8S_NAMESPACE -f mysql/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f mysql/deployment.yaml

kubectl apply -n $K8S_NAMESPACE -f rabbitmq/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f rabbitmq/deployment.yaml



cd ../scripts || exit
