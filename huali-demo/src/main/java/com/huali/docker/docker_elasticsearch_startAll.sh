#!/bin/sh
# 在启动的时候需要将 data 文件夹中先创建 nodes 文件夹 /data/nodes 目录结构 然后在启动
mkdir /home/huali/mydocker/docker-elasticsearch-data/data/nodes
# docker 启动 elasticsearch
docker run -d --name elasticsearch \
	-p 9200:9200 \
	-p 9300:9300 \
	--network ELK \
	-e discovery.type="single-node" \
	-e ES_JAVA_OPTS="-Xms256m -Xmx256m" \
	-v /home/huali/mydocker/docker-elasticsearch-data/data:/usr/share/elasticsearch/data \
	elasticsearch:7.10.1


# 这里的 ik 是将统级目录下 Ik.zip 解压后的整个文件
docker cp ./ik elasticsearch:/usr/share/elasticsearch/plugins
# 然后重启 elasticsearch 服务
docker restart elasticsearch


# docker 启动 kibana
docker run -d --name kibana \
	--link elasticsearch:elasticsearch \
	--network ELK \
	-p 5601:5601 \
	kibana:7.10.1