#!/bin/bash

set -e
set -x

. ./set-env.sh

. ./install-infra.sh

. ./install-apps.sh
