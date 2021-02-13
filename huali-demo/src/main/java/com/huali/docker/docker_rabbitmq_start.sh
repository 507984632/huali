#!/bin/sh
# 进入 rabbitmq 容器中 执行 rabbitmq-plugins enable rabbitmq_management 命令才能访问 15672 的控制台
docker run -d \
	--hostname rabbit \
	--name rabbit \
	-e RABBITMQ_DEFAULT_USER=admin \
	-e RABBITMQ_DEFAULT_PASS=admin \
	-p 5672:5672 \
	-p 15672:15672 \
	rabbitmq
