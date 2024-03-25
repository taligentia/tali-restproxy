#!/bin/sh 

# tail -f /dev/null

#cd /app && java $JAVA_OPTS --illegal-access=debug -jar $RESTPROXY_JAR server config.yml

touch /app/run.log
tail -f /app/run.log

