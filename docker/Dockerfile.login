FROM openjdk:17-jdk-slim

COPY libs /opt/libs

WORKDIR /opt/login
CMD ["sh", "LoginServer_loop.sh"]
