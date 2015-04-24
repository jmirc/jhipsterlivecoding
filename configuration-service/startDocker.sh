#!/usr/bin/env bash

mvn clean package -Pdocker docker:build

#stop docker image
docker stop configuration-service
docker rm configuration-service

# Run the docker image
REPLACE_BY_FULL_PATH=$(pwd)
docker run -p 8888:8888 -v "${REPLACE_BY_FULL_PATH}/src/main/docker/livecoding-config:/livecoding-config" -e "EUREKA_URL=http://192.168.59.103:8761/eureka/" --name configuration-service -t jmirc/configuration-service
