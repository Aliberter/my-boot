mv /var/lib/jenkins/workspace/my-boot/build/libs/my-boot-0.0.1-SNAPSHOT.jar /root/projects/

pid=$(ps -ef | grep my-boot-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $2}')
if [ -n "$pid" ]; then
    kill -9 "$pid";
    echo "stop old project success"
fi

nohup java -agentlib:jdwp=transport=dt_socket,address=5205,server=y,suspend=n -jar /root/projects/my-boot-0.0.1-SNAPSHOT.jar > /root/projects/logs/boot.log 2>&1 &