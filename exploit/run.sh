#!/bin/bash

docker build -t lab4 .
docker run -it --name lab4-container lab4
