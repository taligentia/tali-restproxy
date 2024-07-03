#!/bin/sh 

cd /app
if [ -f "./init.sh" ]; then
    echo "Launch initialisation script"
    dos2unix ./init.sh
    chmod +x ./init.sh
    source ./init.sh
fi
java $JAVA_OPTS --illegal-access=debug -jar $RESTPROXY_JAR server config.yml
#touch /app/run.log && tail -f /app/run.log