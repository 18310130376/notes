#!/bin/bash
source /home/mcloud/.bash_profile
pname=iot-open-api
COUNT=1
KILL_COUNT=6
proPid=/app/${pname}/bin/${pname}.pid
logDir=/app/${pname}/log
JAVA_BIN=java
JAVA_OPTS="-jar -Xms256m -Xmx256m"
MEM_OPTS=" "
GC_OPTS=" "
OPTIMIZE_OPTS=""
JAR_PATH="/app/${pname}/bin/${pname}.jar"
JAVA_CONF="--smart.config.file=/app/iot-open-api/conf/iot-open-api.yml --spring.config.location=/app/iot-open-api/conf/iot-open-api.yml"
JAVA_LOG=""
cd /app/${pname}/bin

export LANG="en_US.UTF-8"
export LC_ALL="en_US.UTF-8"

function start()
{  
    echo "Starting ${pname}: "  
    echo "$JAVA_BIN $JAVA_OPTS $MEM_OPTS $GC_OPTS $OPTIMIZE_OPTS $JAR_PATH $JAVA_CONF $JAVA_LOG > ${logDir}/start.log"
    nohup $JAVA_BIN $JAVA_OPTS $MEM_OPTS $GC_OPTS $OPTIMIZE_OPTS $JAR_PATH $JAVA_CONF $JAVA_LOG > ${logDir}/start.log 2>&1 & echo $! > ${proPid}
    ps -ef |grep -v "grep"|grep "`cat ${proPid}`"
    if [ $? -eq 0 ];then
     echo "${pname} 启动成功"
    else
     echo "${pname} 启动失败"
    fi
}  
   
function stop()
{  
if [ -f ${proPid} ];then
    SPID=`cat ${proPid}`
    kill -9 $SPID
    sleep 3
else
  echo "${pname} pid文件不存在"
fi
}  
   
function restart()
{  
    echo "stoping ..."
    stop  
    start 
}  
   
case "$1" in  
    start)  
        start  
        ;;  
    stop)  
        stop  
        ;;  
    restart)  
        restart  
        ;;  
    *)  
        echo $"Usage: $0 {start|stop|restart}"  
        RETVAL=1  
esac  
exit $RETVAL
