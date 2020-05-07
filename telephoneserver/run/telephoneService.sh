#!/bin/sh
# chkconfig: 345 78 78
# description: Normal RC script for telephoneservice.

. /etc/init.d/functions

PROG=telephoneservice-release-snapshot
PROG_PATH=/opt/telephoneservice
CLASS=net.zdsoft.background.common.ServiceManager

JAVA_HOME=/opt/jdk1.8.0_161
JAVA_PARAM="-Xms512m -Xmx512m -XX:NewSize=64M -XX:MaxNewSize=128M"

start()
{
		cd $PROG_PATH
		pid0=`ps -ef|grep "java"|grep $PROG |awk '{print $2}'`
        if [ "$pid0" = "" ] ; then
			for i in lib/*.jar;
					do CLASS_PATH_LIB=$PWD/$i:"$CLASS_PATH_LIB";
			done
			cd cfg
			$JAVA_HOME/bin/java -server $JAVA_PARAM -Dfile.encoding=GBK -classpath $CLASS_PATH_LIB $CLASS &
			RETVAL=$?
			echo "$PROG startup!"
			return $RETVAL
		else
			echo "$PROG[$pid0] is alived."
			RETVAL=0
			return $RETVAL
		fi
}

stop()
{
        pid0=`ps -ef|grep "java"|grep $PROG|awk '{print $2}'`
        if [ "$pid0" = "" ] ; then
                echo "No $PROG alive."
        else
                kill  $pid0
                echo "Wait for a moment please..."
                sleep 10
                pid=`ps -ef|grep "java"|grep $PROG|awk '{print $2}'`
                if [ "$pid" = "" ] ; then
                        echo "$PROG[$pid0] is stopped."
                        echo "$PROG shutdown."
                else
                        kill -9 $pid
                        echo "$PROG[$pid0] is killed."
                fi
        fi
}

status()
{
        pid0=`ps -ef|grep "java"|grep $PROG |awk '{print $2}'`
        if [ "$pid0" = "" ] ; then
                echo "No $PROG alived."
                RETVAL=1
        else
                echo "$PROG[$pid0] is alived."
                RETVAL=0
        fi
}

case "$1" in
        start)
                start
                ;;
        stop)
                stop
                ;;
        status)
                status
                ;;
        restart)
                stop
                start
                ;;
        help)
                echo "Usage: $0 [OPTION]"
                echo ""
                echo "  start        start $PROG"
                echo "  stop         stop $PROG"
                echo "  help         display this help and exit"
                ;;
        *)
                echo "Usage: $0 {start|stop|status|restart}"
                RETVAL=1
esac

exit $RETVAL
