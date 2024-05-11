#!/bin/bash
############################################
## WARNING!  WARNING!  WARNING!  WARNING! ##
##                                        ##
## DON'T USE NOTEPAD TO CHANGE THIS FILE  ##
## INSTEAD USE SOME DECENT TEXT EDITOR.   ##
## NEWLINE CHARACTERS DIFFER BETWEEN DOS/ ##
## WINDOWS AND UNIX.                      ##
##                                        ##
## USING NOTEPAD TO SAVE THIS FILE WILL   ##
## LEAVE IT IN A BROKEN STATE!!!          ##
############################################

DB_ROOT_PASSWORD='root'

# Update DB URL and Password in login config
sed -i -e "s+^URL=.*+URL=jdbc:mariadb://l2mariadb/l2jdb?useSSL=false+g" ./login/config/LoginServer.properties
sed -i -e "s+Password=.*+Password=$DB_ROOT_PASSWORD+g" ./login/config/LoginServer.properties

# Update LoginHost, DB URL and Password in gameserver config
sed -i -e "s+LoginHost=.*+LoginHost=l2login+g" ./gameserver/config/GameServer.properties
sed -i -e "s+^URL=.*+URL=jdbc:mariadb://l2mariadb/l2jdb?useSSL=false+g" ./gameserver/config/GameServer.properties
sed -i -e "s+Password=.*+Password=$DB_ROOT_PASSWORD+g" ./gameserver/config/GameServer.properties

# Set db root password into docker-compose
sed -i -e "s+      MYSQL_ROOT_PASSWORD:.*+      MYSQL_ROOT_PASSWORD: \"$DB_ROOT_PASSWORD\"+g" ./docker-compose.yml
