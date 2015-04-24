#!/usr/bin/env bash

mvn clean package -Pdocker docker:build

#stop docker image
docker stop registry-service
docker rm --name registry-service

# Run the docker image
docker run -p 8761:8761 --rm --name registry-service -t jmirc/registry-service
