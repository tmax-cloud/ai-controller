#!/bin/sh

export K8S_HOME=/home/tmax/notebook-controller
nohup /usr/bin/java -jar -Dlogback.configurationFile=${K8S_HOME}/logback.xml ${K8S_HOME}/lib/notebook-controller.jar >> ${K8S_HOME}/stdout.log &

tail -f /dev/null