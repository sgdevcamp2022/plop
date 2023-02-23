#!/bin/bash

nohup java -jar discovery-0.0.1-SNAPSHOT.jar --server.port=8761 &
nohup java -jar gateway-0.0.1-SNAPSHOT.jar --server.port=8000 &
nohup java -jar chat-0.0.1-SNAPSHOT.jar --server.port=8011 &
nohup java -jar presence-0.0.1-SNAPSHOT.jar --server.port=8021 &
nohup java -jar auth-0.0.1-SNAPSHOT.jar --server.port=8031 &
nohup java -jar user-0.0.1-SNAPSHOT.jar --server.port=8041 &
nohup java -jar push-0.0.1-SNAPSHOT.jar --server.port=8051 &
