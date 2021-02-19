#!/bin/sh
# 在启动的时候需要将 data 文件夹中先创建 nodes 文件夹 /data/nodes 目录结构 然后在启动
mkdir /home/huali/mydocker/docker-elasticsearch-data/data/nodes

docker run -d --name elasticsearch \
	-p 9200:9200 \
	-p 9300:9300 \
	--network ELK \
	-e discovery.type="single-node" \
	-e ES_JAVA_OPTS="-Xms512m -Xmx512m" \
	-v /home/huali/mydocker/docker-elasticsearch-data/data:/usr/share/elasticsearch/data \
	elasticsearch:7.10.1

