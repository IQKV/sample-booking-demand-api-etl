#!/bin/bash

set -x

. ./set-env.sh

minikube profile $CLUSTER_NAME
minikube ip