#!/usr/bin/env bash

if [ ! -n $JAVA_HOME ]; then
   echo "error: JAVA_HOME is not set"
   exit 1
fi

## 指定应用名称和外部配置路径
## 可通过两种方式指定
## 1、指定spring.application.name='xx' 这时会自动加载 /opt/server_data/xx/application.properties
## 2、指定spring.application.config.location='/myConfigDir/' 这时会自动加载 /myConfigDir/application.properties
OPERATION_OPTS="-Dspring.application.config.location=/opt/server_data/operation-web/"

BIN=`dirname "$0"`
OPERATION_BASE=`cd "$BIN/.." >/dev/null; pwd`

BOOT_HOME="$OPERATION_BASE/boot"
EXEC_JAR_NAME=`ls $BOOT_HOME`
echo "execute jar is: $EXEC_JAR_NAME"

CATALINA_OUT="$OPERATION_BASE/logs/catalina.out"
if [ ! -d "$OPERATION_BASE/logs" ]; then
    mkdir "$OPERATION_BASE/logs"
fi
touch "$CATALINA_OUT"

##构建cmd
START_CMD="java $OPERATION_OPTS -jar $BOOT_HOME/$EXEC_JAR_NAME > $CATALINA_OUT 2>&1 &"
echo "$START_CMD"
_NOHUP="nohup"
eval $_NOHUP $START_CMD