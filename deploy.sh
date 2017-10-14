#!/usr/bin/env bash

scp target/robot-1.0-SNAPSHOT.jar root@aliyun:/root/springboot/
scp target/robot-1.0-SNAPSHOT.jar root@hyberbin.com:/root/springboot/
`ssh root@hyberbin.com "source /etc/profile; sh /root/startRobot.sh">/dev/null 2>&1 &`
#`ssh root@aliyun "sh /root/start.sh ">/dev/null 2>&1 &`
