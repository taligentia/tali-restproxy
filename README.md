# RestProxy

Dans .secrets
- settings-read.xml
- tali-maven-read.txt
- .env


Fichier .secrets/.env

```
USER=...
PASSWD=...
```

## Build
```
$ mvn -e -B -Dmaven.test.skip=true package
$ docker build --no-cache -t restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` --build-arg READPASSWORD=`cat .secrets/tali-maven-read.txt` .
$ docker tag restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` restproxy:latest
```

## Run / Stop / Exec
```
$ mkdir dump && chmod go+rw dump
$ docker run --env-file=.secrets/.env -v "$PWD/dump:/dump" -p 9094:9990 --add-host="win2016-sp.taliwin.com:10.168.0.91" --add-host="win2016-dc.taliwin.com:10.168.0.90" --rm -d --name restproxy restproxy:latest  

$ docker stop restproxy

$ docker exec -i -t restproxy /bin/bash

```

## Sharepoint API test url
```
http://win2016-sp.taliwin.com/sites/kamare/_api/web/title
http://win2016-sp.taliwin.com/sites/kamare/_api/web/lists
http://win2016-sp.taliwin.com/sites/kamare/_api/Web/Lists%28guid%2748d3bd48-4aa1-4a9b-9e19-3c448d1c2351%27%29
```

```
mvn -e -B -Dmaven.test.skip=true package

docker build -t restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` --build-arg READPASSWORD=`cat .secrets/tali-maven-read.txt` .

docker tag restproxy:`cat pom.xml | grep -oP '(?<=<version>).*?(?=</version>)' | head -1` restproxy:latest

docker image ls | grep restproxy

docker run --env-file=.secrets/.env -v "$PWD/dump:/dump" -p 9094:9990 --add-host="win2016-sp.taliwin.com:10.168.0.91" --add-host="win2016-dc.taliwin.com:10.168.0.90" --rm -d --name restproxy restproxy:latest

docker logs restproxy

docker stop restproxy
```

