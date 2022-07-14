#!/bin/bash

set -x

.  ./set-env.sh

kubectl config set-context $CLUSTER_NAME
kubectl config use-context $CLUSTER_NAME

kubectl delete -n $K8S_NAMESPACE deployment mysql
kubectl delete -n $K8S_NAMESPACE service mysql
kubectl delete -n $K8S_NAMESPACE configmap mysql
kubectl delete -n $K8S_NAMESPACE secret mysql

kubectl delete -n $K8S_NAMESPACE deployment rabbitmq
kubectl delete -n $K8S_NAMESPACE service rabbitmq
kubectl delete -n $K8S_NAMESPACE secret rabbitmq
