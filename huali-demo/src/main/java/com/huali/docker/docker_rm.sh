#!/bin/sh
docker rm $(docker container ls -aq)
