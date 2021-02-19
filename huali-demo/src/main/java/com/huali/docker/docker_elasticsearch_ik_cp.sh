#!/bin/sh
# 这里的 ik 是将统级目录下 Ik.zip 解压后的整个文件
docker cp ./ik elasticsearch:/usr/share/elasticsearch/plugins
# 然后重启 elasticsearch 服务
docker restart elasticsearch