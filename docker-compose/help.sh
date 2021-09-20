docker-compose stop
docker-compose rm -f
docker-compose up -d --force-recreate
docker-compose ps

# 重建某一容器
docker-compose up -d --force-recreate yim-gateway-7001
docker-compose up -d --force-recreate yim-auth-server-7002
docker-compose up -d --force-recreate yim-logic-server-7003
docker-compose up -d --force-recreate yim-msg-server-7005
docker-compose up -d --force-recreate yim-ws-server-7110
docker-compose up -d --force-recreate yim-ws-server-7111
docker-compose up -d --force-recreate yim-ws-server-7112