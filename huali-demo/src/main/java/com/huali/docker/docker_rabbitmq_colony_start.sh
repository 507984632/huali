#!/bin/sh
#-v 宿主机中的路径:容器内的路径 将容器内的路径持久化到宿主机中
#RABBITMQ_DEFAULT_USER：rabbitmq 的用户名
#RABBITMQ_DEFAULT_PASS：rabbitmq 的用户密码
#-e RABBITMQ_ERLANG_COOKIE='Rabbit 的Cookie' \
## 该参数 是配置rabbitmq 集群的重要参数，需要每一个rabbit节点都是相同的 Cookie
## 如何找到 cookie 查看容器的 启动日志中的 home dir 路径，这个路径是 存放cookie 的路径 一般cookie 的文件名都叫做 .erlang.cookie
## docker 容器命令 docker logs 容器名称 例如 docker logs rabbit | grep home 可以直接打印出 home 相关的行
#--link name(容器名称):hostname(服务器名称)
##如 三个借点 【容器名称(服务器名称)】  rabbit(rabbit) rabbit2(rabbit2) rabbit3(rabbit3)
##rabbit  中不需要添加 --link 参数，因为这个是第一个启动的，
##rabbit2 中需要添加 --link rabbit:rabbit \ 参数， 显示写上 rabbit 节点的容器名，服务器名称
##rabbit3 中需要添加 --link rabbit2:rabbit2 \ 参数，原因同上
#rabbitmq 该参数是指定 docker 容器中的镜像名称
#
# 进入容器内的操作
#进入 rabbitmq 容器中 执行 rabbitmq-plugins enable rabbitmq_management 命令才能访问 15672 的控制台
#搭建集群却看不到 节点
## 主节点
### 1. rabbitmqctl stop_app
### 2. rabbitmqctl reset
### 3. rabbitmqctl start_app
## 从节点(所有的都是)
### 1. rabbitmqctl stop_app
### 2. rabbitmqctl reset
### 3. rabbitmqctl join_cluster --ram rabbit@rabbit
####参数“--ram”表示设置为内存节点，忽略次参数默认为磁盘节点。
####rabbit@ 是固定的， rabbit (master 的主机名[是主节点的服务器名]) 如果不是同一条机器需要在 /etc/host文件中添加对应的节点
### 4. rabbitmqctl start_app
## 测试成不成功 可以在任意节点 rabbitmqctl cluster_status 命令执行
docker run -d \
  --hostname rabbit \
  --name rabbit \
  -p 5672:5672 \
  -p 15672:15672 \
  --restart=always \
  -v /home/huali/mydocker/docker-rabbit-data/lib:/var/lib/rabbitmq \
  -v /home/huali/mydocker/docker-rabbit-data/log:/var/log/rabbitmq \
  -e RABBITMQ_DEFAULT_USER=admin \
  -e RABBITMQ_DEFAULT_PASS=admin \
  -e RABBITMQ_ERLANG_COOKIE='WWYJUMKFOVSUKNQMIXOR'
# 	--link rabbit:rabbit \
# 	--link rabbit2:rabbit2 \
rabbitmq
