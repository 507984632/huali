#!/bin/sh
docker run -d --name kibana \
	--link elasticsearch:elasticsearch \
	--network ELK \
	-p 5601:5601 \
	kibana:7.10.1
