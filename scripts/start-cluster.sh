#!/bin/bash

set -e
set -x

. ./set-env.sh

minikube start -p $CLUSTER_NAME --memory='6000mb' --cpus=4 --disk-size=40g --vm-driver="docker" --insecure-registry=localhost:5000
minikube profile $CLUSTER_NAME
minikube addons enable ingress
minikube addons enable dashboard
minikube addons enable metrics-server

eval $(minikube docker-env)

kubectl config use-context $CLUSTER_NAME
minikube -p $CLUSTER_NAME tunnel dashboard
