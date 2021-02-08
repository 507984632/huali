#!/bin/sh
docker run -p 6379:6379 --name myRedis\
	-v /home/huali/mydocker/docker-redis-data/redis.conf:/etc/redis/redis.conf \
	-v /home/huali/mydocker/docker-redis-data/data:/data \
	-d redis \
	redis-server /etc/redis/redis.conf \
	--appendonly yes
