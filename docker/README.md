# Solution to start the server in docker

## 1. Put docker files into compiled project directory.

## 2. Before starting docker-compose file need to :

a. If you need to change sql root password update `DB_ROOT_PASSWORD` variable in `configs_updater.sh`
b. Run `configs_updater.sh` to update properties files
c. If you are planning to connect to the server from different locations:
    update InternalHostname and ExternalHostname  `gameserver\config\GameServer.propertie`

## 3. Build the containers:
```
docker-compose build
```
## 4. Start the containers:
```
docker-compose up -d
```

## 5. Add sql data by using scripts from the project
After the first start login to l2adminer (Open in a browser `IP-address:8081`, for example `127.0.0.1:8081`.)
or directly to mariadb server and create databases (use sql files from a build)


Restart the server:
```
docker-compose down -t0
docker-compose up -d
```