PATH_JAR="./manager-0.0.1-SNAPSHOT.jar"
PID_PATH_NAME="./pid"
case $1 in
    stop)
        echo "stoped spring-boot"
        if [ -f $PID_PATH_NAME ]; then
            PID=$(cat $PID_PATH_NAME);
            echo "spting-boot stoping ..."
            kill $PID;
            echo "spting-boot stopped ..."
            rm $PID_PATH_NAME
        else
            echo "$SERVICE_NAME is not running ..."
        fi
    ;;
    start)
        echo "strted spring-boot"
        pathJar=$(pwd)/$(ls | grep *.jar)
        echo $pathJar
        nohup java -jar $pathJar > data.log 2> error.log& 
        echo $!> $PID_PATH_NAME
    ;;
    restart)
        pathJar=$(pwd)/$(ls | grep *.jar)
        echo $pathJar
        eval $(pwd)/$0 stop
        eval $(pwd)/$0 start

    ;;
esac
