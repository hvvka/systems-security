#!/bin/bash

docker build -t lab5 .
docker run --cap-add=SYS_PTRACE --security-opt seccomp=unconfined -it --name lab5-container lab5
