#!/bin/sh
docker run -p 3306:3306 --name mysql-5.7\
	-v /home/huali/mydocker/docker-mysql-data/conf:/etc/mysql \
	-v /home/huali/mydocker/docker-mysql-data/logs:/var/log/mysql \
	-v /home/huali/mydocker/docker-mysql-data/data:/var/lib/mysql \
	-e MYSQL_ROOT_PASSWORD=123456 \
	-d mysql:5.7
