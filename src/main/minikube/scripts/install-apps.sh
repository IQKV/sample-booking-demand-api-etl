#!/bin/bash

# set -e
set -x

. ./set-env.sh


#minikube addons enable ingress

cd ../manifests || exit

kubectl config use-context $CLUSTER_NAME
kubectl apply -n $K8S_NAMESPACE -f init-container/configmap.yaml
kubectl apply -n $K8S_NAMESPACE -f init-container/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f init-container/deployment.yaml

kubectl apply -n $K8S_NAMESPACE -f edge/configmap.yaml
kubectl apply -n $K8S_NAMESPACE -f edge/deployment.yaml

kubectl apply -n $K8S_NAMESPACE -f dashboard/configmap.yaml
kubectl apply -n $K8S_NAMESPACE -f dashboard/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f dashboard/deployment.yaml
kubectl apply -n $K8S_NAMESPACE -f importer/configmap.yaml
kubectl apply -n $K8S_NAMESPACE -f importer/secret.yaml
kubectl apply -n $K8S_NAMESPACE -f importer/deployment.yaml
kubectl apply -n $K8S_NAMESPACE -f ingress.yaml

# set Minikupe IP for bcdb.info in /etc/hosts
minikube profile $CLUSTER_NAME
CLUSTER_IP=$(minikube ip)
echo $CLUSTER_IP
sudo sed -i.bak 's/.*bcdb.info/'"$CLUSTER_IP"' bcdb.info/' /etc/hosts && sudo rm /etc/hosts.bak
echo "$(minikube ip) bcdb.info" | sudo tee -a /etc/hosts

cd ../scripts || exit
