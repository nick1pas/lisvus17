# Docker files for the server

## Put docker files into compiled project directory

## Before starting docker-compose file need to :

1. Patch `gameserver\config\GameServer.propertie`s (change mysql server address):
```
URL=jdbc:mariadb://l2mariadb/l2jdb?useSSL=false
```
2. Patch `login\config\LoginServer.properties` (change mysql server address):
```
URL=jdbc:mariadb://l2mariadb/l2jdb?useSSL=false
```
3. Patch `login\LoginServer_loop.sh` (for linux - change '==' to-eq) :
```
until [ $err -eq 0 ];
```
4. IF you need to change sql root password update `MYSQL_ROOT_PASSWORD` property od docker-compose file according to your properties files

5. Build the containers:
```
docker-compose build
```
5. Start the containers:
```
docker-compose up -d
```

##
After the first start login to l2adminer (via http on port 8081 on the machine were it was started) or directly to mariadb server and create databases (use sql files from a build)
Open in a browser `IP-address:8081`, for example `127.0.0.1:8081`.
Restart the server:
```
docker-compose down -t0
docker-compose up -d
```