#!/bin/bash

set -x

. ./set-env.sh

. ./delete-all.sh

kubectl config set-context $CLUSTER_NAME
kubectl config use-context $CLUSTER_NAME

kubectl delete clusterrolebinding service-pod-reader-$K8S_NAMESPACE

kubectl delete serviceaccount $SVC_NAME -n $K8S_NAMESPACE

kubectl delete clusterrole microservices-kubernetes-namespace-reader

kubectl delete namespace $K8S_NAMESPACE
