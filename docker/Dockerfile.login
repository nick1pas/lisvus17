FROM openjdk:17-jdk-slim

COPY login /opt/login
COPY libs /opt/libs

WORKDIR /opt/login
CMD ["sh", "LoginServer_loop.sh"]
