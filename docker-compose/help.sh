docker-compose stop
docker-compose rm -f
docker-compose up -d --force-recreate
docker-compose ps

# 重启基础容器
docker-compose restart yim-nginx
docker-compose restart yim-mysql
docker-compose restart yim-redis
docker-compose restart yim-zookeeper

# 停止业务容器
docker-compose stop yim-gateway-7001
docker-compose stop yim-auth-server-7002
docker-compose stop yim-logic-server-7003
docker-compose stop yim-msg-server-7005
docker-compose stop yim-ws-server-7110
docker-compose stop yim-ws-server-7111
docker-compose stop yim-ws-server-7112

# 重建业务容器
docker-compose up -d --force-recreate yim-gateway-7001
docker-compose up -d --force-recreate yim-auth-server-7002
docker-compose up -d --force-recreate yim-logic-server-7003
docker-compose up -d --force-recreate yim-msg-server-7005
docker-compose up -d --force-recreate yim-ws-server-7110
docker-compose up -d --force-recreate yim-ws-server-7111
docker-compose up -d --force-recreate yim-ws-server-7112

# 删除所有 jar
rm -rf yim-ws-server-7110/ws-server-1.0-SNAPSHOT.jar
rm -rf yim-ws-server-7111/ws-server-1.0-SNAPSHOT.jar
rm -rf yim-ws-server-7112/ws-server-1.0-SNAPSHOT.jar
rm -rf yim-logic-server-7003/logic-server-1.0-SNAPSHOT.jar
rm -rf yim-msg-server-7005/msg-server-1.0-SNAPSHOT.jar
rm -rf yim-gateway-7001/gateway-1.0-SNAPSHOT.jar
rm -rf yim-auth-server-7002/auth-server-1.0-SNAPSHOT.jar