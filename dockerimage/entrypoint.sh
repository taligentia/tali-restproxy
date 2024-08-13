#!/bin/bash 

echo ">>> Apply environment variables in configuration files"
for filename in /app/templates/*; do
    #echo "$filename"
    if [[ -f $filename ]]; then
        echo "--- envsubst $filename"
        envsubst -i $filename -o /app/$(basename "$filename")
        cat /app/$(basename "$filename")
    fi
done

#for filename in /app/templates_override/*; do
#    #echo "$filename"
#    if [[ -f $filename ]]; then
#        echo "---- envsubst $filename"
#        envsubst -i $filename -o /app/$(basename "$filename")
#        cat /app/$(basename "$filename")
#    fi
#done

cd /app
if [ -f "./init.sh" ]; then
    echo ">>>  Initialisation script detected"
    cp ./init.sh ./init2.sh
    dos2unix ./init2.sh 2> /dev/null
    chmod +x ./init2.sh
    source ./init2.sh
fi

echo ">>> Starting application"
java $JAVA_OPTS -jar $RESTPROXY_JAR server config.yml
touch /app/run.log && tail -f /app/run.log