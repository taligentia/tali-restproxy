#!/bin/sh 

cd /app
if [ -f "./init.sh" ]; then
    echo "Launch initialisation script"
    cp ./init.sh ./init2.sh
    dos2unix ./init2.sh 2> /dev/null
    chmod +x ./init2.sh
    source ./init2.sh
fi
java $JAVA_OPTS --illegal-access=debug -jar $RESTPROXY_JAR server config.yml
#touch /app/run.log && tail -f /app/run.log